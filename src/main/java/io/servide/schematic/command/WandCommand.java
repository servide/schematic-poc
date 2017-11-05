package io.servide.schematic.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.voldex.common.inject.discover.Discover;
import net.voldex.common.inject.initialize.Initialize;
import net.voldex.common.spigot.command.CommandContext;
import net.voldex.common.spigot.command.Commands;

@Discover
public class WandCommand {

	@Initialize
	private void init()
	{
		Commands.create("wand")
				.assertPlayer()
				.handler(this::handle);
	}

	private void handle(CommandContext<Player> context)
	{
		Player player = context.getSender();

		player.getInventory().addItem(new ItemStack(Material.GOLD_AXE));
	}

}
