package nl.naxanria.headhunters.event;

import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.event.player.IPlayerPickupItemEvent;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.Buff;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.entity.RunsafeItem;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerPickupItemEvent;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

import nl.naxanria.headhunters.handler.PlayerHandler;

public class PlayerItemPickUp implements IPlayerPickupItemEvent
{
	public PlayerItemPickUp(IOutput console, PlayerHandler playerHandler)
	{
		this.console = console;
		this.playerHandler = playerHandler;
	}

	@Override
	public void OnPlayerPickupItemEvent(RunsafePlayerPickupItemEvent event)
	{

		IPlayer player = event.getPlayer();
		RunsafeItem item = event.getItem();
		RunsafeMeta usingItem = item.getItemStack();

		if (playerHandler.isIngame(player))
		{
			console.broadcastColoured(String.format("%s picked up %d", player.getName(), item.getItemStack().getItemId()));

			boolean used = false;
			if (usingItem.is(Item.Food.Golden.Apple))
			{
				Buff.Healing.Regeneration.amplification(2).duration(4).applyTo(player);
				used = true;
			}
			else if (usingItem.is(Item.Materials.Sugar))
			{
				Buff.Utility.Movement.IncreaseSpeed.duration(4).amplification(4).applyTo(player);
				used = true;
			}

			if (used)
			{
				event.getItem().remove();
				event.setCancelled(true);
			}
		}


	}

	private final IOutput console;
	private final PlayerHandler playerHandler;
}
