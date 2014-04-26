package nl.naxanria.headhunters.event;

import no.runsafe.framework.api.event.player.IPlayerDamageEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.entity.RunsafeEntityDamageEvent;
import nl.naxanria.headhunters.handler.PlayerHandler;
import org.bukkit.Effect;

public class DamagePlayer implements IPlayerDamageEvent
{
	public DamagePlayer(PlayerHandler playerHandler)
	{
		this.playerHandler = playerHandler;
	}

	@Override
	public void OnPlayerDamage(IPlayer player, RunsafeEntityDamageEvent event)
	{
		if (playerHandler.isIngame(player)) //todo: not deprecated way?
			player.getWorld().playEffect(player.getLocation(), Effect.getById(2001), 152);
	}

	private final PlayerHandler playerHandler;
}
