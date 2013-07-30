package nl.naxanria.headhunters.command;

import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import nl.naxanria.headhunters.Core;

import java.util.HashMap;
import java.util.Map;

public class CommandJoin extends PlayerCommand
{
	public CommandJoin(Core core)
	{
		super("join", "Join a headhunters game", "headhunters.play");
		this.core = core;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, Map<String, String> parameters)
	{
		return this.core.join(executor);
	}

	private final Core core;
}
