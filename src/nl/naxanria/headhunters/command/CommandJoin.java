package nl.naxanria.headhunters.command;

import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import nl.naxanria.headhunters.Core;

public class CommandJoin extends PlayerCommand
{
	public CommandJoin(Core core)
	{
		super("join", "Join a headhunters game", "headhunters.play");
		this.core = core;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		return this.core.join(executor);
	}

	private final Core core;
}
