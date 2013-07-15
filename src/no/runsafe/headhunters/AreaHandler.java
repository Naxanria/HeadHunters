package no.runsafe.headhunters;

import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.entity.RunsafeEntity;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AreaHandler
{
    public AreaHandler()
	{
		areas = new HashMap<Integer, SimpleArea>();
		currentArea = 0;
		nextArea = 0;
		availableRegions = new StringBuilder();
	}

	public void loadAreas(List<String> areaList)
	{
		areasList = (ArrayList<String>) areaList;
		areas.clear();
		int index = 0;
		boolean first = true;
        availableRegions = new StringBuilder();

        for (String area : areaList)
        {
            SimpleArea simpleArea = new SimpleArea(RunsafeServer.Instance.getWorld(world), area);
            areas.put(index, simpleArea);
            if (!first)
				availableRegions.append(",");
            else
				first = false;
            availableRegions.append(simpleArea.getRegionName());
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
		if (newNextArea < 0)
			newNextArea = 0;

		nextArea = newNextArea;
	}

	public int getAreaByName(String name)
	{
		for (int i = 0; i < areas.size(); i++)
			if (areas.get(i).getRegionName().equalsIgnoreCase(name))
				return i;

		return -1;
	}

	public RunsafeLocation getSafeLocation()
	{
		return areas.get(currentArea).safeLocation();
	}

	public void setWorld(String world)
	{
		this.world = world;
	}

	public String getWorld()
	{
		return world;
	}

	public int getAmountLoadedAreas()
	{
		return areas.size();
	}

	public String getAvailableRegions()
	{
		return availableRegions.toString();
	}

	public void teleport(int region, RunsafePlayer player)
	{
		areas.get(region).teleportToArea(player);
	}

	public boolean isInCombatRegion(RunsafeLocation location)
	{
		for (int i = 0; i < areas.size(); i++)
			if (areas.get(i).pointInArea(location))
				return true;

		return false;
	}

	public boolean isInCurrentCombatRegion(RunsafeLocation location)
	{
		return isInCombatRegion(location, currentArea);
	}

	public boolean isInCombatRegion(RunsafeLocation location, int area)
	{
		return areas.get(area).pointInArea(location);
	}

	public ArrayList<String> get__areas__()
	{
		return areasList;
	}

	public void removeEntities(List<RunsafeEntity> entities)
	{
		if (areas.containsKey(currentArea))
			for (RunsafeEntity entity : entities) //lets not delete players...
				if (!(entity instanceof RunsafePlayer) && areas.get(currentArea).pointInArea(entity.getLocation()))
					entity.remove();
	}

    public void setWaitRoom(String region)
    {
        waitRoom = new SimpleArea(RunsafeServer.Instance.getWorld(world), region);
    }

    public SimpleArea getWaitRoom()
	{
        return waitRoom;
    }

    public boolean isInWaitRoom(RunsafePlayer player){
        return waitRoom.pointInArea(player.getLocation());
    }

    public RunsafeLocation getWaitRoomSpawn()
    {
        return waitroomSpawn;
    }

    public ArrayList<RunsafePlayer> getWaitRoomPlayers()
    {
        return waitRoom.getPlayers();
    }

    public ArrayList<RunsafePlayer> getWaitRoomPlayers(GameMode mode)
    {
        return waitRoom.getPlayers(mode);
    }

    public void setWaitRoomSpawn(RunsafeLocation location)
    {
        waitroomSpawn = location;
    }

	private final HashMap<Integer, SimpleArea> areas;
	private StringBuilder availableRegions;
	private int currentArea;
	private int nextArea;
	private String world = "world";
    private SimpleArea waitRoom;
	private ArrayList<String> areasList;
    private RunsafeLocation waitroomSpawn;
}
