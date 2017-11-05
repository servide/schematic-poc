package io.servide.schematic.player;

import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.inject.Singleton;

import net.voldex.common.inject.initialize.Initialize;
import net.voldex.common.spigot.event.Listen;

@Singleton
public final class PlayerRegistry {

	private final Map<Player, WrappedPlayer> players = new WeakHashMap<>();

	@Initialize
	private void init()
	{
		Listen.to(PlayerJoinEvent.class)
				.handle(this::registerPlayer);

		Listen.to(PlayerQuitEvent.class)
				.handle(this::deregisterPlayer);

		Bukkit.getServer().getOnlinePlayers().forEach(online -> this.players.put(online, new WrappedPlayer(online)));
	}

	public WrappedPlayer getWrapped(Player player)
	{
		return this.players.get(player);
	}

	private void registerPlayer(PlayerJoinEvent event)
	{
		this.players.put(event.getPlayer(), new WrappedPlayer(event.getPlayer()));
	}

	private void deregisterPlayer(PlayerQuitEvent event)
	{
		this.players.remove(event.getPlayer());
	}

}
