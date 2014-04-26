package nl.naxanria.headhunters;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.block.IBlock;
import no.runsafe.framework.api.block.ISign;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeLocation;

import no.runsafe.worldguardbridge.WorldGuardInterface;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;

public class SimpleArea
{
	public SimpleArea(IWorld world, String regionName)
	{
		this.world = world;
		this.regionName = regionName;

		if (worldGuardInterface != null && worldGuardInterface.serverHasWorldGuard())
		{
			region = worldGuardInterface.getRegion(world, regionName);
		}
	}

    public boolean pointInArea(ILocation location)
	{
		return pointInArea(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public boolean pointInArea(int x, int y, int z)
	{
		return region.contains(x, y, z);
	}

	public ArrayList<IPlayer> getPlayers()
	{
		ArrayList<IPlayer> players = new ArrayList<IPlayer>();

		for (IPlayer player : this.world.getPlayers())
			if (pointInArea(player.getLocation())) players.add(player);

		return players;
	}

	public ArrayList<IPlayer> getPlayers(GameMode mode)
	{
		ArrayList<IPlayer> players = new ArrayList<IPlayer>();

		for (IPlayer player : this.world.getPlayers())
			if (pointInArea(player.getLocation()) && player.getGameMode() == mode) players.add(player);

		return players;
	}

	public double getMinX()
	{
		return region.getMinimumPoint().getX();
	}

	public double getMaxX()
	{
		return region.getMaximumPoint().getX();
	}

	public double getMinY()
	{
		return region.getMinimumPoint().getY();
	}

	public double getMaxY()
	{
		return region.getMaximumPoint().getY();
	}

	public double getMinZ()
	{
		return region.getMinimumPoint().getZ();
	}

	public double getMaxZ()
	{
		return region.getMaximumPoint().getZ();
	}

	public RunsafeLocation getCenter()
	{
		return new RunsafeLocation(
				new Location(
					(World) world,
					(getMaxX() - getMinX()) / 2,
					(getMaxY() - getMinY()) / 2,
					(getMaxZ() - getMinY()) / 2
				)
		);
	}

	@Override
	public String toString()
	{
		return String.format(
			"[%.4f,%.4f,%.4f - %.4f,%.4f,%.4f @%s]",
			region.getMinimumPoint().getX(),
			region.getMinimumPoint().getY(),
			region.getMinimumPoint().getZ(),
			region.getMaximumPoint().getX(),
			region.getMaximumPoint().getY(),
			region.getMaximumPoint().getZ(),
			world.getName()
		);
	}

	public void teleportToArea(IPlayer player)
	{
		player.teleport(world, this.getMinX(), this.getMaxY() + 15, this.getMinZ());
	}

	public RunsafeLocation safeLocation()
	{
		int x, y, z;

		int minY = (int) this.getMinY();
		int maxY = (int) this.getMaxY();
		int tries = 2000;
		boolean foundGround;
		while (tries > 0)
		{
			foundGround = false;

			x = Util.getRandom((int) this.getMinX() + 3, (int) this.getMaxX() - 3);
			z = Util.getRandom((int) this.getMinZ() + 3, (int) this.getMaxZ() - 3);

			int air = 0;
			for (y = minY; y < maxY - 1; y++)
			{
				IBlock block = world.getBlockAt(x, y, z);
				if (block.getMaterial().getType() == Material.SIGN)
				{
					ISign sign = (ISign) block;
					if (sign.getLine(0).equalsIgnoreCase("skip"))
						continue;
				}
				if (!block.isAir())
				{
					air = 0;
					foundGround = true;
				}
				else
				{
					air++;
					if (foundGround && air > 1)
						return new RunsafeLocation(new Location((World) world, x + 0.5, (double) y - 1, z + 0.5));
				}
			}
			tries--;
		}
		return null;
	}

	public String getRegionName()
	{
		return this.regionName;
	}

	public static void setWorldGuardInterface(WorldGuardInterface worldGuardInterface)
	{
		SimpleArea.worldGuardInterface = worldGuardInterface;
	}


  private static WorldGuardInterface worldGuardInterface;
	private ProtectedRegion region;
	private final IWorld world;
	private final String regionName;

}