package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Util;
import nl.naxanria.headhunters.database.AreaRepository;
import nl.naxanria.headhunters.database.WaitRoomRepository;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import nl.naxanria.headhunters.handler.AreaHandler;
import no.runsafe.worldguardbridge.WorldGuardInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandSetWaitroom extends PlayerCommand
{
	public CommandSetWaitroom(AreaHandler areaHandler, WorldGuardInterface worldGuardInterface, AreaRepository areaRepository, WaitRoomRepository waitRoomRepository)
	{
		super("waitroom", "Define a region as the wait room", "headhunters.regions.modify.waitroom");
		this.areaHandler = areaHandler;
		this.worldGuardInterface = worldGuardInterface;
		this.areaRepository = areaRepository;
		this.waitRoomRepository = waitRoomRepository;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, HashMap<String, String> parameters)
	{

		ArrayList<String> areas = areaRepository.getAreas();
		List<String> region = worldGuardInterface.getRegionsAtLocation(executor.getLocation());
		if (region.size() == 0)
			return "No region found";
		if (region.size() > 1)
			return "Multiple regions found";

		String thisRegion = region.get(0);

		if (Util.arrayListContainsIgnoreCase(areas, thisRegion))
			return "Region is registered as a combat region";

		waitRoomRepository.setWaitRoom(thisRegion, executor.getWorld().getName());
		areaHandler.setWaitRoom(thisRegion, executor.getWorld());
		return "&aSuccesfully set headhunters waitroom as &f" + thisRegion;
	}

	private final AreaHandler areaHandler;
	private final WorldGuardInterface worldGuardInterface;
	private final AreaRepository areaRepository;
	private final WaitRoomRepository waitRoomRepository;
}
