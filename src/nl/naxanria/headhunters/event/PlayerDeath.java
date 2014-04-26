package nl.naxanria.headhunters.event;

import net.minecraft.server.v1_7_R2.EnumClientCommand;
import net.minecraft.server.v1_7_R2.PacketPlayInClientCommand;

import nl.naxanria.headhunters.Core;
import nl.naxanria.headhunters.Util;
import nl.naxanria.headhunters.handler.PlayerHandler;
import nl.naxanria.headhunters.RandomItem;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.player.IPlayerDeathEvent;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.extension.player.RunsafePlayer;
import no.runsafe.framework.minecraft.Item;

import no.runsafe.framework.minecraft.event.player.RunsafePlayerDeathEvent;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

import org.bukkit.craftbukkit.v1_7_R2.entity.CraftPlayer;

import java.util.List;

public class PlayerDeath implements IPlayerDeathEvent
{
	public PlayerDeath(Core core, RandomItem randomItem, PlayerHandler playerHandler, IScheduler scheduler)
	{
		this.core = core;
		this.randomItem = randomItem;
		this.playerHandler = playerHandler;
		this.scheduler = scheduler;
	}

	@Override
	public void OnPlayerDeathEvent(RunsafePlayerDeathEvent event)
	{

		final RunsafePlayer player = event.getEntity();

		if (playerHandler.isIngame(player))
		{
			IPlayer killer = player.getKiller();
			if (killer != null)
				killer.setHealth(Math.min(killer.getHealth() + 4, 20));

			event.setDroppedXP(0);
			List<RunsafeMeta> items = event.getDrops();
			int amount = Util.amountMaterial(items, Item.Decoration.Head.Human.getItem());

			List<RunsafeMeta> toDrop = randomItem.getCleanedDrops(items);
			items.clear();
			items.addAll(toDrop);
			items.add(randomItem.get());
			event.setDrops(items);
			RunsafeMeta heads = Item.Decoration.Head.Human.getItem();

			heads.setAmount(amount + 1);
			player.getWorld().dropItem(
				player.getEyeLocation(),
				heads
			);


			//autorespawning
			scheduler.startSyncTask(new Runnable()
			{
				@Override
				public void run()
				{

					PacketPlayInClientCommand packetPlayInClientCommand = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);

					((CraftPlayer) player.getRaw()).getHandle().playerConnection.a(packetPlayInClientCommand);


				}
			}, 10L);

		}
	}

	private final RandomItem randomItem;
	private final Core core;
	private final PlayerHandler playerHandler;
	private final IScheduler scheduler;
}