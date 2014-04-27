package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Constants;
import nl.naxanria.headhunters.Core;
import nl.naxanria.headhunters.Util;
import nl.naxanria.headhunters.database.AreaRepository;

import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgument;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;

import no.runsafe.framework.text.ChatColour;
import nl.naxanria.headhunters.handler.AreaHandler;
import no.runsafe.worldguardbridge.WorldGuardInterface;

import javax.annotation.Nonnull;
import java.util.ArrayList;

import java.util.List;


public class CommandSetCombatArea extends PlayerCommand
{
	public CommandSetCombatArea(Core core, AreaHandler areaHandler, WorldGuardInterface worldGuardInterface,
															AreaRepository areaRepository)
	{
		super("combatarea", "Adds or removes the WorldGuard region you are in as a combat area.", "headhunters.regions.modify.areas", new RequiredArgument("p"));
		this.core = core;
		this.worldGuardInterface = worldGuardInterface;
		this.areaHandler = areaHandler;
		this.areaRepository = areaRepository;

		arguments = new ArrayList<IArgument>();
		arguments.add(new RequiredArgument("add"));
		arguments.add(new RequiredArgument("del"));
	}

	@Nonnull
	@Override
	public String getUsageCommandParams(ICommandExecutor executor)
	{
		return ChatColour.YELLOW + this.getName() + "&f<" + ChatColour.YELLOW + "add " + ChatColour.RESET + "|" + ChatColour.YELLOW + " del" + ChatColour.RESET + ">";
	}

	@Nonnull
	@Override
	public List<IArgument> getParameters()
	{
		return arguments;
	}

	@Override
	public String OnExecute(IPlayer player, IArgumentList parameters)
	{
		boolean add;

		String arg = parameters.get("p");
		if (arg.equalsIgnoreCase("add"))
			add = true;
		else if (arg.equalsIgnoreCase("del"))
			add = false;
		else
			return this.getUsage(player);

		if (core.isEnabled()) return Constants.ERROR_COLOR + "Only use this when headhunters is disabled!";

		if (areaHandler.isInGameWorld(player))
		{

			List<String> regions = worldGuardInterface.getRegionsAtLocation(player.getLocation());

			if (regions == null || regions.size() == 0)
				return Constants.ERROR_COLOR + "No region found";
			if (regions.size() > 1)
				return Constants.ERROR_COLOR + "Found multiple regions";

			ArrayList<String> areas = areaRepository.getAreas();
			String thisRegion = regions.get(0);


			if (areaHandler.getWaitRoomName().equalsIgnoreCase(thisRegion))
				return Constants.ERROR_COLOR + "This region is registered as the waitroom";

			if (add)
			{
				if (Util.arrayListContainsIgnoreCase(areas, thisRegion))
					return Constants.ERROR_COLOR + "Region already exists";

				areas.add(thisRegion);
				areaHandler.loadAreas(areas);
				areaRepository.addArea(thisRegion);

				return Constants.MSG_COLOR + "Added region &f" + thisRegion;
			}
			else
			{
				if (!Util.arrayListContainsIgnoreCase(areas, thisRegion))
					return Constants.ERROR_COLOR + "Region does not exist as a combat area.";

				Util.arrayListRemoveIgnoreCase(areas, thisRegion);
				areaHandler.loadAreas(areas);
				areaRepository.delArea(thisRegion);

				return Constants.MSG_COLOR + "Succesfully removed region &f" + thisRegion;
			}
		}
		else
		{
			return Constants.ERROR_COLOR + "Please move to the correct world";
		}
	}

	private final Core core;
	private final WorldGuardInterface worldGuardInterface;
	private final AreaHandler areaHandler;
	private final AreaRepository areaRepository;
	private ArrayList<IArgument> arguments;


}
