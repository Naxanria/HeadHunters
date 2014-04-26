package nl.naxanria.headhunters.command;

import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import nl.naxanria.headhunters.handler.PlayerHandler;

public class CommandLeave extends PlayerCommand
{
	public CommandLeave(PlayerHandler playerHandler)
	{
		super("leave", "Leaves the current game", "headhunters.play");

		this.playerHandler = playerHandler;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		playerHandler.remove(executor);
		return null;
	}

	private PlayerHandler playerHandler;
}
