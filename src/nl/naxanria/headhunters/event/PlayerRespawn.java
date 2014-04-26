package nl.naxanria.headhunters.event;

import nl.naxanria.headhunters.handler.PlayerHandler;
import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.event.player.IPlayerRespawn;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.RunsafeLocation;
import nl.naxanria.headhunters.handler.AreaHandler;

public class PlayerRespawn implements IPlayerRespawn
{
	public PlayerRespawn(PlayerHandler playerHandler, AreaHandler areaHandler)
	{

		this.playerHandler = playerHandler;
		this.areaHandler = areaHandler;
	}

	@Override
	public RunsafeLocation OnPlayerRespawn(IPlayer player, ILocation location, boolean isBed)
	{
		if (playerHandler.isIngame(player))
		{
			playerHandler.setUpPlayer(player);
			player.teleport(areaHandler.getSafeLocation());
			return areaHandler.getSafeLocation();
		}
		return null;
	}

	private final PlayerHandler playerHandler;
	private final AreaHandler areaHandler;
}
