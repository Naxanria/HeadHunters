package nl.naxanria.headhunters.event;

import nl.naxanria.headhunters.Core;
import no.runsafe.framework.api.event.block.IBlockBreakEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.block.RunsafeBlockBreakEvent;
import nl.naxanria.headhunters.handler.AreaHandler;
import nl.naxanria.headhunters.handler.PlayerHandler;

public class BlockBreak implements IBlockBreakEvent
{
	public BlockBreak(PlayerHandler playerHandler, Core core, AreaHandler areaHandler)
	{
		this.playerHandler = playerHandler;
		this.core = core;
		this.areaHandler = areaHandler;
	}

	@Override
	public void OnBlockBreakEvent(RunsafeBlockBreakEvent event)
	{
		IPlayer eventPlayer = event.getPlayer();
		if (eventPlayer.getWorld().getName().equalsIgnoreCase(playerHandler.getWorldName()))
			if (areaHandler.isInCombatRegion(eventPlayer.getLocation()))
				if (core.isEnabled() || !eventPlayer.hasPermission("headhunters.build"))
					event.cancel();
	}

	private final PlayerHandler playerHandler;
	private final Core core;
	private final AreaHandler areaHandler;
}
