package no.runsafe.headhunters.event;

import no.runsafe.framework.api.event.player.IPlayerRightClick;
import no.runsafe.framework.minecraft.Buff;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.block.RunsafeBlock;
import no.runsafe.framework.minecraft.entity.ProjectileEntity;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import no.runsafe.headhunters.AreaHandler;
import no.runsafe.headhunters.PlayerHandler;
import no.runsafe.headhunters.Util;
import org.bukkit.Effect;
import org.bukkit.Sound;

import java.util.ArrayList;

public class PlayerRightClick implements IPlayerRightClick
{
	public PlayerRightClick(RunsafeServer server, PlayerHandler playerHandler, AreaHandler areaHandler)
	{
		this.server = server;
		this.playerHandler = playerHandler;
		this.areaHandler = areaHandler;
	}

	@Override
	public boolean OnPlayerRightClick(RunsafePlayer player, RunsafeMeta usingItem, RunsafeBlock targetBlock)
	{
		int range = 5;
		int roll = 95;

		if (playerHandler.isIngame(player))
		{
			boolean used = false;

			if (usingItem != null)
			{
				RunsafeLocation location = (targetBlock != null) ? targetBlock.getLocation() : player.getLocation();

				if (usingItem.is(Item.Miscellaneous.Slimeball))
				{
					//visual effect...
					server.getWorld(playerHandler.getWorldName()).playEffect(location, Effect.POTION_BREAK, 16426);

					ArrayList<RunsafePlayer> hitPlayers = playerHandler.getIngamePlayers(location, range);
					for (RunsafePlayer hitPlayer : hitPlayers)
					{
						if (!hitPlayer.getName().equalsIgnoreCase(player.getName()))
						{
							Buff.Utility.Movement.DecreaseSpeed.duration(3).amplification(5).applyTo(hitPlayer);
							Buff.Utility.DigSpeed.Decrease.duration(6).amplification(5);
						}
					}
					used = true;
				}
				else if (usingItem.is(Item.Brewing.MagmaCream))
				{
					ArrayList<RunsafePlayer> hitPlayers = playerHandler.getIngamePlayers(location, range);
					for (RunsafePlayer hitPlayer : hitPlayers)
					{
						if (!hitPlayer.getName().equalsIgnoreCase(player.getName()))
						{
							hitPlayer.strikeWithLightning(true);
							hitPlayer.setHealth(Math.max(hitPlayer.getHealth() - 4, 0));
							hitPlayer.setFireTicks(90);
						}
					}
					used = true;
				}
				else if (usingItem.is(Item.Materials.NetherStar))
				{
					used = true;

					if (Util.actPercentage(roll))
					{
						RunsafeLocation newLocation = areaHandler.getSafeLocation();
						if (newLocation != null)
							player.teleport(newLocation);
						else
							playerHandler.remove(player);
					}
					else
					{
						server.getWorld(playerHandler.getWorldName()).createExplosion(player.getLocation(), 2f, false, false);
					}
				}
				else if (usingItem.is(Item.Materials.BlazeRod))
				{
					player.Launch(ProjectileEntity.Fireball);
					RunsafeServer.Instance.getWorld(playerHandler.getWorldName()).getRaw().playSound(player.getLocation().getRaw(), Sound.GHAST_FIREBALL, 1f, 1f);
					used = true;
				}
				else if (usingItem.is(Item.Materials.InkSack))
				{
					ArrayList<RunsafePlayer> hitPlayers = playerHandler.getIngamePlayers(location, range);
					for (RunsafePlayer hitPlayer : hitPlayers)
					{
						if (!hitPlayer.getName().equalsIgnoreCase(player.getName()))
						{
							Buff.Combat.Blindness.duration(3).amplification(6).applyTo(hitPlayer);
							Buff.Combat.Damage.Decrease.duration(3).amplification(3).applyTo(hitPlayer);
						}
					}
					used = true;
				}
				else if (usingItem.is(Item.Brewing.GhastTear))
				{
					playerHandler.teleportAllPlayers(player.getLocation());
					player.teleport(areaHandler.getSafeLocation());
					used = true;
				}
			}

			if (used)
			{
				RunsafeMeta items = player.getItemInHand();
				items.remove(1);

				player.getInventory().setItemInHand(items);
			}
			return !used;
		}
		return true;
	}

	private final RunsafeServer server;
	private final PlayerHandler playerHandler;
	private final AreaHandler areaHandler;
}
