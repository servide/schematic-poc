package io.servide.schematic.command;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;

import io.servide.schematic.Schempoc;
import io.servide.schematic.player.PlayerRegistry;
import io.servide.schematic.player.WrappedPlayer;
import io.servide.schematic.select.CubeSelection;
import io.servide.schematic.nbt.BaseBlock;
import io.servide.schematic.nbt.ByteArrayTag;
import io.servide.schematic.nbt.CompoundTag;
import io.servide.schematic.nbt.IntTag;
import io.servide.schematic.nbt.ListTag;
import io.servide.schematic.nbt.NBTOutputStream;
import io.servide.schematic.nbt.ShortTag;
import io.servide.schematic.nbt.StringTag;
import io.servide.schematic.nbt.Tag;
import net.voldex.common.except.Try;
import net.voldex.common.inject.discover.Discover;
import net.voldex.common.inject.initialize.Initialize;
import net.voldex.common.spigot.command.CommandContext;
import net.voldex.common.spigot.command.Commands;

@Discover
public class CreateSchematicCommand {

	@Inject
	private Schempoc plugin;
	@Inject
	private PlayerRegistry registry;

	@Initialize
	private void init()
	{
		Commands.create("createschem")
				.assertPlayer()
				.assertExactArgs(1)
				.handler(this::handle);
	}

	private void handle(CommandContext<Player> context)
	{
		Player player = context.getSender();
		WrappedPlayer wrapped = this.registry.getWrapped(player);

		File file = new File(this.plugin.getDataFolder(), context.argument(0));

		if (file.exists())
		{
			player.sendMessage("Schematic file already exists with name " + context.argument(0));
			return;
		}

		if (wrapped.getPosLeft() == null || wrapped.getPosRight() == null ||
				!wrapped.getPosLeft().getWorld().getUID().equals(wrapped.getPosRight().getWorld().getUID()))
		{
			player.sendMessage("Error with selection, please use /wand");
			return;
		}

		CubeSelection selection = CubeSelection.fromLocations(wrapped.getPosLeft(), wrapped.getPosRight());

		World world = wrapped.getPosLeft().getWorld();
		int width = selection.getSize().getBlockX();
		int height = selection.getSize().getBlockY();
		int length = selection.getSize().getBlockZ();

		player.sendMessage("Schematic is [" + width + "X * " + height + "Y * " + length + "Z]");

		Stopwatch stopwatch = Stopwatch.createStarted();

		Map<String, Tag> schematic = new HashMap<>();
		schematic.put("Width", new ShortTag((short) width));
		schematic.put("Height", new ShortTag((short) height));
		schematic.put("Length", new ShortTag((short) length));
		schematic.put("Materials", new StringTag("Alpha"));

		byte[] blocks = new byte[width * height * length];
		byte[] blockData = new byte[width * height * length];
		List<Tag> tileEntities = new ArrayList<>();

		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () ->
		{
			byte[] addBlocks = null;

			for (int x = 0; x < width; x++)
			{
				player.sendMessage("Progress: [" + x + " / " + (width - 1) + "]");

				for (int y = 0; y < height; y++)
				{
					for (int z = 0; z < length; z++)
					{
						int index = y * width * length + z * width + x;
						BaseBlock block = new BaseBlock(this.blockAt(world, selection.getOrigin(), x, y, z));

						// Save 4096 IDs in an AddBlocks section
						if (block.getType() > 255)
						{
							if (addBlocks == null)
							{ // Lazily create section
								addBlocks = new byte[(blocks.length >> 1) + 1];
							}

							addBlocks[index >> 1] = (byte) (((index & 1) == 0) ?
									addBlocks[index >> 1] & 0xF0 | (block.getType() >> 8) & 0xF
									: addBlocks[index >> 1] & 0xF | ((block.getType() >> 8) & 0xF) << 4);
						}

						blocks[index] = (byte) block.getType();
						blockData[index] = (byte) block.getData();

						// Get the list of key/values from the block
						CompoundTag rawTag = block.getNbtData();
						if (rawTag != null)
						{
							Map<String, Tag> values = new HashMap<>();

							for (Map.Entry<String, Tag> entry : rawTag.getValue().entrySet())
							{
								values.put(entry.getKey(), entry.getValue());
							}

							values.put("id", new StringTag(block.getNbtId()));
							values.put("x", new IntTag(x));
							values.put("y", new IntTag(y));
							values.put("z", new IntTag(z));

							CompoundTag tileEntityTag = new CompoundTag(values);
							tileEntities.add(tileEntityTag);
						}
					}
				}
			}

			schematic.put("Blocks", new ByteArrayTag(blocks));
			schematic.put("Data", new ByteArrayTag(blockData));
			schematic.put("Entities", new ListTag(CompoundTag.class, new ArrayList<Tag>()));
			schematic.put("TileEntities", new ListTag(CompoundTag.class, tileEntities));

			if (addBlocks != null)
			{
				schematic.put("AddBlocks", new ByteArrayTag(addBlocks));
			}

			Bukkit.getScheduler().runTask(this.plugin, () ->
					this.buildSchematic(file, player, schematic, stopwatch));
		});
	}

	private void buildSchematic(File file, Player player, Map<String, Tag> schematic, Stopwatch stopwatch)
	{
		// Build and output
		Try.to(() ->
		{
			file.createNewFile();

			CompoundTag schematicTag = new CompoundTag(schematic);
			NBTOutputStream stream = new NBTOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
			stream.writeNamedTag("Schematic", schematicTag);
			stream.close();
		});

		player.sendMessage("Saved yo!");
		player.sendMessage("Took: " + stopwatch.stop());

	}

	private int distance(int x0, int x1)
	{
		return Math.max(x0, x1) - Math.min(x0, x1);
	}

	private Block blockAt(World world, Vector origin, int offsetX, int offsetY, int offsetZ)
	{
		int x = origin.getBlockX() + offsetX;
		int y = origin.getBlockY() + offsetY;
		int z = origin.getBlockZ() + offsetZ;

		return world.getBlockAt(x, y, z);
	}

}
