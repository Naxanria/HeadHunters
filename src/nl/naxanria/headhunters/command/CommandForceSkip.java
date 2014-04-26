package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Constants;
import nl.naxanria.headhunters.Core;
import nl.naxanria.headhunters.handler.VoteHandler;
import no.runsafe.framework.api.command.argument.IArgument;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.OptionalArgument;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import nl.naxanria.headhunters.handler.AreaHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandForceSkip extends PlayerCommand
{
	public CommandForceSkip(AreaHandler areaHandler, VoteHandler voteHandler, Core core)
	{
		super("forceskip", "skips the current map for another one", "headhunters.game-control.forceskip", new RequiredArgument("map"));
		this.core = core;
		this.areaHandler = areaHandler;
		this.voteHandler = voteHandler;
	}


	public List<String> getParameterOptions(String parameter)
	{
		ArrayList<String> parameterOptions = new ArrayList<String>();
		parameterOptions.addAll(areaHandler.getAvailableRegionsAsList());
		parameterOptions.add("random");
		return parameterOptions;
	}

	@Nonnull
	@Override
	public List<IArgument> getParameters()
	{
		ArrayList<String> parameterOptions = new ArrayList<String>();
		parameterOptions.addAll(areaHandler.getAvailableRegionsAsList());
		parameterOptions.add("random");

		List<IArgument> arguments = new ArrayList<IArgument>();
		for(String option : parameterOptions)
		{
			arguments.add(new OptionalArgument(option));
		}

		return arguments;
	}

	@Override
	public String OnExecute(IPlayer executor,IArgumentList parameters)
	{
		if (core.isEnabled())
		{
			String nextMap = parameters.get("map");
			int nextMapIndex;
			if (nextMap != null && nextMap.equalsIgnoreCase("random"))
				if (areaHandler.getAmountLoadedAreas() > 1)
					areaHandler.randomNextArea();
				else
					return Constants.ERROR_COLOR + "Define at least 2 areas to be able to use random.";

			else if ((nextMapIndex = areaHandler.getAreaByName(nextMap)) != -1)
				areaHandler.setNextArea(nextMapIndex);
			else
				return Constants.ERROR_COLOR + "Please specify a correct map, or use &frandom" + Constants.ERROR_COLOR +
					" for a random map. Available:&f" + areaHandler.getAvailableRegions();
			voteHandler.resetVotes();
			return "&bNext region will be:&f" + areaHandler.getAreaName(areaHandler.getNextArea());
		}
		return Constants.ERROR_COLOR + "Only use this when headhunters is enabled!";
	}

	private final Core core;
	private final AreaHandler areaHandler;
	private final VoteHandler voteHandler;
}
