package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.handler.AreaHandler;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;

public class CommandSetSpawn extends PlayerCommand
{
	public CommandSetSpawn(AreaHandler areaHandler)
	{
		super("spawn", "Sets the spawn point for in the wait room", "headhunters.regions.modify.spawn");
		this.areaHandler = areaHandler;
	}

	@Override
	public String OnExecute(IPlayer player, IArgumentList parameters) {
		areaHandler.setWaitRoomSpawn(player.getLocation());
		return "Successfully set spawn";
	}

	private final AreaHandler areaHandler;

}
