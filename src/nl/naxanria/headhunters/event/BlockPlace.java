package nl.naxanria.headhunters.event;

import nl.naxanria.headhunters.handler.AreaHandler;
import nl.naxanria.headhunters.Core;
import nl.naxanria.headhunters.handler.PlayerHandler;
import no.runsafe.framework.api.block.IBlock;
import no.runsafe.framework.api.event.block.IBlockPlace;
import no.runsafe.framework.api.player.IPlayer;

public class BlockPlace implements IBlockPlace
{
	public BlockPlace(Core core, PlayerHandler playerHandler, AreaHandler areaHandler)
	{
		this.core = core;
		this.playerHandler = playerHandler;
		this.areaHandler = areaHandler;
	}

	@Override
	public boolean OnBlockPlace(IPlayer eventPlayer, IBlock block)
	{

		if (eventPlayer.getWorld().getName().equalsIgnoreCase(playerHandler.getWorldName()))
			if (areaHandler.isInCombatRegion(eventPlayer.getLocation()))
				if (core.isEnabled() || !eventPlayer.hasPermission("headhunters.build"))
					return false;

		return true;

	}

	private final Core core;
	private final PlayerHandler playerHandler;
	private final AreaHandler areaHandler;
}
