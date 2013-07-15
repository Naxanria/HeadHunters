package no.runsafe.headhunters.command;

import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.headhunters.Constants;
import no.runsafe.headhunters.Core;
import no.runsafe.headhunters.PlayerHandler;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandShowIngame extends PlayerCommand
{
	public CommandShowIngame(PlayerHandler playerHandler)
	{
		super("show", "shows ingame players", "headhunters.play");
		this.playerHandler = playerHandler;
	}

	@Override
	public String OnExecute(RunsafePlayer executor, HashMap<String, String> parameters)
	{
		List<String> players = new ArrayList<String>();
		for (RunsafePlayer player : playerHandler.getIngamePlayers())
			players.add(player.getPrettyName());

		return Constants.MSG_COLOR + Strings.join(players, ", ");
	}

	private final PlayerHandler playerHandler;
}
