package nl.naxanria.headhunters.event;

import no.runsafe.framework.api.event.player.IPlayerTeleportEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerTeleportEvent;
import nl.naxanria.headhunters.handler.AreaHandler;
import nl.naxanria.headhunters.handler.PlayerHandler;

public class PlayerTeleport implements IPlayerTeleportEvent
{
	public PlayerTeleport(PlayerHandler playerHandler, AreaHandler areaHandler)
	{
		this.playerHandler = playerHandler;
		this.areaHandler = areaHandler;
	}

	@Override
	public void OnPlayerTeleport(RunsafePlayerTeleportEvent event)
	{
		IPlayer player = event.getPlayer();

		if (playerHandler.isIngame(player))
			if (!areaHandler.isInCurrentCombatRegion(event.getTo()))
				playerHandler.remove(player);
	}

	private final PlayerHandler playerHandler;
	private final AreaHandler areaHandler;
}
