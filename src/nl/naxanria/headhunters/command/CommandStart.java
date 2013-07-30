package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Core;
import no.runsafe.framework.api.command.argument.OptionalArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.HashMap;
import java.util.Map;

public class CommandStart extends PlayerCommand
{
	public CommandStart(Core core)
	{
		super("start", "Forces headhunters match to start", "headhunters.game-control.start", new OptionalArgument("time"));
		this.core = core;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, Map<String, String> parameters)
	{
		int time = (parameters.get("time") != null) ? Integer.valueOf(parameters.get("time")) : 0;

		return core.startInTime(time);
	}

	private final Core core;
}
