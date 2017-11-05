package io.servide.schematic.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WrappedPlayer {

	private final Player player;

	private Location posLeft;
	private Location posRight;

	public WrappedPlayer(Player player)
	{
		this.player = player;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Location getPosLeft()
	{
		return this.posLeft;
	}

	public void setPosLeft(Location posLeft)
	{
		this.posLeft = posLeft;
	}

	public Location getPosRight()
	{
		return this.posRight;
	}

	public void setPosRight(Location posRight)
	{
		this.posRight = posRight;
	}

}
