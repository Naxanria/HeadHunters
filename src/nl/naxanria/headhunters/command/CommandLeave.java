package nl.naxanria.headhunters.command;

import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import nl.naxanria.headhunters.handler.PlayerHandler;

import java.util.HashMap;
import java.util.Map;

public class CommandLeave extends PlayerCommand
{
	public CommandLeave(PlayerHandler playerHandler)
	{
		super("leave", "Leaves the current game", "headhunters.play");

		this.playerHandler = playerHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, Map<String, String> parameters)
	{
		playerHandler.remove(executor);
		return null;
	}

	private final PlayerHandler playerHandler;
}
