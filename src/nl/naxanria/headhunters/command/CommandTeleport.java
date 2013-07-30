package nl.naxanria.headhunters.command;

import no.runsafe.framework.api.command.argument.RequiredArgument;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import nl.naxanria.headhunters.handler.AreaHandler;
import nl.naxanria.headhunters.Constants;
import nl.naxanria.headhunters.handler.PlayerHandler;
import org.bukkit.GameMode;

import java.util.List;
import java.util.Map;

public class CommandTeleport extends PlayerCommand
{
	public CommandTeleport(PlayerHandler playerHandler, AreaHandler areaHandler)
	{
		super("teleport", "teleports you to a given area", "headhunters.regions.teleport", new RequiredArgument("region"));
		this.playerHandler = playerHandler;
		this.areaHandler = areaHandler;
	}

	@Override
	public List<String> getParameterOptions(String parameter)
	{
		return areaHandler.getAvailableRegionsAsList();
	}

	@Override
	public String getUsageCommandParams()
	{
		return "<map> &aAvailable maps: &f" + areaHandler.getAvailableRegions() + "\n";
	}

	@Override
	public String OnExecute(RunsafePlayer executor, Map<String, String> parameters)
	{
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

	private final PlayerHandler playerHandler;
	private final AreaHandler areaHandler;
}
