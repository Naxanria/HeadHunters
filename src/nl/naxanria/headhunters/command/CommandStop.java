package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Core;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;

public class CommandStop extends PlayerCommand
{
	public CommandStop(Core core)
	{
		super("stop", "Stops the current game", "headhunters.game-control.stop");
		this.core = core;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters) {
		this.core.stop(executor);
		return null;
	}

	private final Core core;

}
