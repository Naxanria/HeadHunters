package nl.naxanria.headhunters.handler;

import nl.naxanria.headhunters.SimpleArea;
import nl.naxanria.headhunters.Util;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.IWorld;
import no.runsafe.framework.api.entity.IEntity;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.extension.RunsafeServer;
import no.runsafe.framework.internal.extension.player.RunsafePlayer;
import no.runsafe.framework.minecraft.RunsafeLocation;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AreaHandler implements IConfigurationChanged
{
	public AreaHandler(RunsafeServer server)
	{
		currentArea = 0;
		nextArea = 0;
		this.server = server;
	}

	public void loadAreas(List<String> areaList)
	{
		areas.clear();
		availableRegions.clear();
		availableRegions.addAll(areaList);
		int index = 0;
		for (String area : areaList)
		{
			SimpleArea simpleArea = new SimpleArea(world, area);
			areas.put(index, simpleArea);
			index++;
		}
	}

	public int getCurrentArea()
	{
		return currentArea;
	}

	public void setNextAsCurrentArea()
	{
		currentArea = nextArea;
		if (!areas.containsKey(currentArea))
			currentArea = 0;
	}

	public int getNextArea()
	{
		return nextArea;
	}

	public String getAreaName(int index)
	{
		return areas.get(index).getRegionName();
	}

	public int randomNextArea()
	{
		return (nextArea = Util.getRandom(0, areas.size(), nextArea));
	}

	public void setNextArea(int newNextArea)
	{
		if (newNextArea < 0) newNextArea = 0;
		nextArea = newNextArea;
	}

	public int getAreaByName(String name)
	{
		for (int i = 0; i < areas.size(); i++)
		{
			if (areas.get(i).getRegionName().equalsIgnoreCase(name))
				return i;
		}
		return -1;
	}

	public RunsafeLocation getSafeLocation()
	{
		return areas.get(currentArea).safeLocation();
	}

	public IWorld getWorld()
	{
		return world;
	}

	public int getAmountLoadedAreas()
	{
		return areas.size();
	}

	public String getAvailableRegions()
	{
		return Strings.join(availableRegions, ", ");
	}

	public List<String> getAvailableRegionsAsList() {
		return availableRegions;
	}
	public void teleport(int region, IPlayer player)
	{
		areas.get(region).teleportToArea(player);
	}

	public boolean isInCombatRegion(ILocation location)
	{
		for (int i = 0; i < areas.size(); i++)
		{
			if (areas.get(i).pointInArea(location)) return true;
		}
		return false;
	}

	public boolean isInCurrentCombatRegion(ILocation location)
	{
		return isInCombatRegion(location, currentArea);
	}

	public boolean isInCombatRegion(ILocation location, int area)
	{
		return areas.get(area).pointInArea(location);
	}

	public void removeEntities()
	{
		if (areas.containsKey(currentArea))
			for (IEntity entity : world.getEntities()) //lets not delete players...
				if (!(entity instanceof RunsafePlayer) && areas.get(currentArea).pointInArea(entity.getLocation()))
					entity.remove();
	}

    public boolean initWaitRoom(SimpleArea waitRoom)
    {

        this.waitRoom = waitRoom;

        return true;
    }

	public void setWaitRoom(String region, IWorld world)
	{
		if (waitRoom != null && waitRoom.getRegionName().equalsIgnoreCase(region))
			return;
		waitRoom = new SimpleArea(world, region);
	}

	public boolean isInGameWorld(IPlayer player)
	{
		return world.equals(player.getWorld());
	}

	public boolean isInWaitRoom(IPlayer player)
	{
		return waitRoom.pointInArea(player.getLocation());
	}

	public ILocation getWaitRoomSpawn()
	{
		return waitroomSpawn;
	}

	public String getWaitRoomName()
	{
		return waitRoom.getRegionName();
	}

	public ArrayList<IPlayer> getWaitRoomPlayers(GameMode mode)
	{
		return waitRoom.getPlayers(mode);
	}

	public void setWaitRoomSpawn(ILocation location)
	{
		waitroomSpawn = location;
		configuration.setConfigValue("waitingroomspawn.x", location.getBlockX());
		configuration.setConfigValue("waitingroomspawn.y", location.getBlockY());
		configuration.setConfigValue("waitingroomspawn.z", location.getBlockZ());
		configuration.setConfigValue("waitingroom-world", location.getWorld().getName());
		configuration.save();
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.configuration = configuration;
		world = server.getWorld(configuration.getConfigValueAsString("world"));

		waitroomSpawn = new RunsafeLocation();
				server.getWorld(configuration.getConfigValueAsString("waitingroom-world")),
				configuration.getConfigValueAsDouble("waitingroomspawn.x"),
				configuration.getConfigValueAsDouble("waitingroomspawn.y"),
				configuration.getConfigValueAsDouble("waitingroomspawn.z")
			);
	}

	private final HashMap<Integer, SimpleArea> areas = new HashMap<Integer, SimpleArea>();
	private final List<String> availableRegions = new ArrayList<String>();
	private IConfiguration configuration;
	private int currentArea;
	private int nextArea;
	private IWorld world;
	private SimpleArea waitRoom;
	private ILocation waitroomSpawn;
	private RunsafeServer server;


}
