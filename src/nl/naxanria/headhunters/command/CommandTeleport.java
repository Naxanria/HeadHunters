package nl.naxanria.headhunters.command;

import no.runsafe.framework.api.command.argument.IArgument;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;
import nl.naxanria.headhunters.handler.AreaHandler;
import nl.naxanria.headhunters.Constants;
import nl.naxanria.headhunters.handler.PlayerHandler;

import org.bukkit.GameMode;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;


public class CommandTeleport extends PlayerCommand
{
	public CommandTeleport(PlayerHandler playerHandler, AreaHandler areaHandler)
	{
		super("teleport", "teleports you to a given area", "headhunters.regions.teleport", new RequiredArgument("region"));
		this.playerHandler = playerHandler;
		this.areaHandler = areaHandler;
	}

	@Nonnull
	@Override
	public List<IArgument> getParameters()
	{

		List<IArgument> arguments = new ArrayList<IArgument>();
		for(String option : areaHandler.getAvailableRegionsAsList())
			arguments.add(new RequiredArgument(option));
		return arguments;
	}

	public String getUsageCommandParams()
	{
		return "<map> &aAvailable maps: &f" + areaHandler.getAvailableRegions() + "\n";
	}

	private final PlayerHandler playerHandler;
	private final AreaHandler areaHandler;

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters) {
		if (playerHandler.isIngame(executor))
		{
			return Constants.ERROR_COLOR + "You can not use this command while in game!";
		}

		String region = parameters.get("region");
		int regionId = areaHandler.getAreaByName(region);
		if (regionId == -1)
			return getUsageCommandParams();
		areaHandler.teleport(regionId, executor);
		executor.setGameMode(GameMode.CREATIVE);
		return null;
	}
}
