package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.*;
import nl.naxanria.headhunters.handler.AreaHandler;
import nl.naxanria.headhunters.handler.PlayerHandler;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.player.IPlayer;

import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;

import java.util.ArrayList;

public class CommandInfo extends ExecutableCommand
{
	public CommandInfo(Core core, PlayerHandler playerHandler, AreaHandler areaHandler)
	{
		super("info", "shows info about the current game", "headhunters.play");
		this.core = core;
		this.playerHandler = playerHandler;
		this.areaHandler = areaHandler;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, IArgumentList parameters)
	{
		ArrayList<String> info = new ArrayList<String>();
		if (core.isEnabled())
		{
			info.add("-------------");

			if (core.getGamestarted())
			{
				info.add("Current headhunters match info");
				int min = (int) Math.floor(core.getTimeToEnd() / 60);
				int sec = core.getTimeToEnd() - min * 60;
				info.add(String.format("Time remaining &f%s:%s", min, Util.fillZeros(sec)));
				IPlayer leader = playerHandler.getCurrentLeader();
				if (leader != null) info.add(String.format("Current leader: %s", leader.getPrettyName()));
				info.add(String.format("Heads needed: &f%d", playerHandler.getWinAmount()));
				info.add(String.format("Current Map: &f%s", areaHandler.getAreaName(areaHandler.getCurrentArea())));
			}
			else
			{
				int min = (int) Math.floor(core.getTimeToStart() / 60);
				int sec = core.getTimeToStart() % 60;
				info.add(String.format("Game will start in &f%d:%s", min, Util.fillZeros(sec)));
				info.add(String.format(Constants.MSG_NEXT_MAP, areaHandler.getAreaName(areaHandler.getNextArea())));
			}
			info.add("------------");

			return Strings.join(info, "\n");
		}
		return Constants.ERROR_COLOR + "Headhunters is disabled";
	}

	private final Core core;
	private final PlayerHandler playerHandler;
	private final AreaHandler areaHandler;


}
