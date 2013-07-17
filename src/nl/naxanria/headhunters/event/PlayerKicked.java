package nl.naxanria.headhunters.event;

import no.runsafe.framework.api.event.player.IPlayerQuitEvent;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerQuitEvent;
import nl.naxanria.headhunters.handler.PlayerHandler;

public class PlayerKicked implements IPlayerQuitEvent
{
	public PlayerKicked(PlayerHandler playerHandler)
	{
		this.playerHandler = playerHandler;
	}

	@Override
	public void OnPlayerQuit(RunsafePlayerQuitEvent event)
	{
		if (playerHandler.isIngame(event.getPlayer()))
			playerHandler.remove(event.getPlayer());
	}

	private final PlayerHandler playerHandler;
}