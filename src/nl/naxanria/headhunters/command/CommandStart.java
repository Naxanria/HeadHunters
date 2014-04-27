package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Core;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.OptionalArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;

public class CommandStart extends PlayerCommand
{
	public CommandStart(Core core)
	{
		super("start", "Forces headhunters match to start", "headhunters.game-control.start", new OptionalArgument("time"));
		this.core = core;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		int time = (parameters.get("time") != null) ? Integer.valueOf(parameters.get("time")) : 0;

		return core.startInTime(time);
	}

	private final Core core;
}
