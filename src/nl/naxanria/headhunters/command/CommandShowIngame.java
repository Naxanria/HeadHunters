package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Constants;
import nl.naxanria.headhunters.handler.PlayerHandler;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;

import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import java.util.ArrayList;
import java.util.List;


public class CommandShowIngame extends PlayerCommand
{
	public CommandShowIngame(PlayerHandler playerHandler)
	{
		super("show", "shows ingame players", "headhunters.play");
		this.playerHandler = playerHandler;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		List<String> players = new ArrayList<String>();
		for (IPlayer player : playerHandler.getIngamePlayers())
			players.add(player.getPrettyName());
		return Constants.MSG_COLOR + Strings.join(players, ", ");
	}

	private final PlayerHandler playerHandler;

}
