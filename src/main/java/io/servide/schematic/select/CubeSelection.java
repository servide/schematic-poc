package io.servide.schematic.select;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class CubeSelection {

	public static CubeSelection fromLocations(Location loc1, Location loc2)
	{
		return CubeSelection.fromVectors(loc1.toVector(), loc2.toVector());
	}

	public static CubeSelection fromVectors(Vector vector1, Vector vector2)
	{
		int x1 = vector1.getBlockX();
		int y1 = vector1.getBlockY();
		int z1 = vector1.getBlockZ();

		int x2 = vector2.getBlockX();
		int y2 = vector2.getBlockY();
		int z2 = vector2.getBlockZ();

		Vector origin = new Vector(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));
		Vector max = new Vector(Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
		Vector size = max.subtract(origin);

		return new CubeSelection(origin, size);
	}

	private final Vector origin;
	private final Vector size;

	private CubeSelection(Vector origin, Vector size)
	{
		this.origin = origin;
		this.size = size;
	}

	public Vector getOrigin()
	{
		return this.origin;
	}

	public Vector getSize()
	{
		return this.size;
	}

}
