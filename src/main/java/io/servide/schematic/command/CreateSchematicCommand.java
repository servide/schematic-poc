package io.servide.schematic.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.inject.Inject;

import io.servide.schematic.player.PlayerRegistry;
import io.servide.schematic.player.WrappedPlayer;
import net.voldex.common.inject.discover.Discover;
import net.voldex.common.inject.initialize.Initialize;
import net.voldex.common.spigot.command.CommandContext;
import net.voldex.common.spigot.command.Commands;

@Discover
public class CreateSchematicCommand {

	@Inject
	private PlayerRegistry registry;

	@Initialize
	private void init()
	{
		Commands.create("createschem")
				.assertPlayer()
				.handler(this::handle);
	}

	private void handle(CommandContext<Player> context)
	{
		Player player = context.getSender();
		WrappedPlayer wrapped = this.registry.getWrapped(player);

		Location pos1 = wrapped.getPosLeft();
		Location pos2 = wrapped.getPosRight();


	}

}
