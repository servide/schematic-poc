package io.servide.schematic.listen;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.inject.Inject;

import io.servide.schematic.player.PlayerRegistry;
import io.servide.schematic.player.WrappedPlayer;
import net.voldex.common.inject.discover.Discover;

@Discover
public class WandListener implements Listener {

	@Inject
	private PlayerRegistry registry;

	@EventHandler
	public void on(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();

		if (player.getItemInHand().getType() != Material.GOLD_AXE)
		{
			return;
		}

		Action action = event.getAction();

		if (event.getClickedBlock() == null || action == Action.LEFT_CLICK_AIR || action == Action.RIGHT_CLICK_AIR)
		{
			return;
		}

		WrappedPlayer wrapped = this.registry.getWrapped(player);

		if (action == Action.LEFT_CLICK_BLOCK)
		{
			wrapped.setPosLeft(event.getClickedBlock().getLocation());
			player.sendMessage("Set left-click position to " + event.getClickedBlock().getLocation());
		}
		else
		{
			wrapped.setPosRight(event.getClickedBlock().getLocation());
			player.sendMessage("Set right-click position to " + event.getClickedBlock().getLocation());
		}
	}

}
