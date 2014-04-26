package nl.naxanria.headhunters.handler;

import nl.naxanria.headhunters.Constants;
import nl.naxanria.headhunters.RandomItem;
import nl.naxanria.headhunters.Util;
import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.extension.RunsafeServer;
import no.runsafe.framework.minecraft.Buff;
import no.runsafe.framework.minecraft.Item;

import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;

import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerHandler
{
	public PlayerHandler(EquipmentHandler manager, AreaHandler areaHandler, ScoreboardHandler scoreboardHandler, RandomItem randomItem, IOutput console, RunsafeServer server)
	{
		playerData = new HashMap<String, Boolean>();
		this.equipmentHandler = manager;
		this.areaHandler = areaHandler;
    this.scoreboardHandler = scoreboardHandler;
		this.randomItem = randomItem;
		this.console = console;
		this.server = server;
	}

	public boolean isWinner()
	{
		return winner;
	}

	public int getWinAmount()
	{
		return winAmount;
	}

	public String getWorldName()
	{
		return areaHandler.getWorld().getName();
	}

	public void remove(IPlayer player)
	{
		if (isIngame(player))
		{
			playerData.put(player.getName(), true);
      scoreboardHandler.removeScoreBoard(player);
			if(areaHandler.isInCurrentCombatRegion(player.getLocation()))
			{
				unEquip(player);
				RunsafeMeta heads = Item.Decoration.Head.Human.getItem();
				heads.setAmount(Util.amountMaterial(player, heads));
				player.getWorld().dropItem(player.getEyeLocation(), heads);
				ArrayList<RunsafeMeta> toDrop = randomItem.getCleanedDrops(player.getInventory().getContents());
				for(RunsafeMeta meta : toDrop)
					player.getWorld().dropItem(player.getEyeLocation(), meta);

				player.getInventory().clear();
				player.teleport(areaHandler.getWaitRoomSpawn());
			}
			ArrayList<IPlayer> ingame = getIngamePlayers();

			if (ingame.size() == 1)
			{
				winner = true;
				leader = ingame.get(0);
			}
		}
	}

	public boolean isIngame(IPlayer player)
	{
		String playerName = player.getName();
		return areaHandler.isInGameWorld(player) && playerData.containsKey(playerName) && !playerData.get(playerName);
	}


	public void addPlayer(IPlayer player)
	{
		playerData.put(player.getName(), false);
		scoreboardHandler.addScoreboard(player);
		scoreboardHandler.updateScoreboard(player, 0);
	}

	public void addPlayers(ArrayList<IPlayer> players)
	{
		for (IPlayer player : players)
			addPlayer(player);
	}

	public void teleportAllPlayers(ILocation location)
	{
		for (String playerName : playerData.keySet())
			if (!playerData.get(playerName))
				teleport(server.getPlayerExact(playerName), location); //todo server
	}

	public void teleport()
	{
		for (String playerName : playerData.keySet())
			if (!this.playerData.get(playerName))
				teleport(server.getPlayerExact(playerName), areaHandler.getSafeLocation());
	}

	public void teleport(IPlayer player, ILocation location)
	{
			player.teleport(location);
	}

	public void start(ArrayList<IPlayer> players)
	{
		addPlayers(players);
		unEquipAll();
		teleport();
		setUpPlayers();
		winAmount = (int) ((players.size() / 2) + players.size() * 3.5);
		scoreboardHandler.updateScoreBoardCaption(winAmount);
	}

	public void setUpPlayers()
	{
		for (String playerName : playerData.keySet())
			this.setUpPlayer(server.getPlayerExact(playerName));
	}

	public void setUpPlayer(IPlayer player)
	{
			player.setGameMode(GameMode.SURVIVAL);
			equipmentHandler.equip(player);
			player.setSaturation(10f);
			player.setHealth(20f);
			player.setFoodLevel(20);
			player.removeBuffs();
			Buff.Resistance.Damage.amplification(2).duration(6).applyTo(player);
	}

	public void reset()
	{
		winAmount = 0;
		leader = null;
		playerData.clear();
		winner = false;
	}

	public ArrayList<String> tick()
	{
		ArrayList<String> out = new ArrayList<String>();
		int currLAmount = 0;
		IPlayer currLeader = null;
		for (String playerName : playerData.keySet())
		{
			if (!this.playerData.get(playerName))
			{
				IPlayer player = server.getPlayerExact(playerName);
				int amount = Util.amountMaterial(player, Item.Decoration.Head.Human.getItem());
				if (amount != 0 && amount > leaderAmount && amount > currLAmount)
				{
					currLeader = player;
					currLAmount = amount;
				}
        scoreboardHandler.updateScoreboard(player, amount);
				player.setSaturation(10f);
				if (isIngame(player) && !player.getWorld().getName().equalsIgnoreCase(getWorldName()))
				{
					remove(player);
					player.sendColouredMessage("&3You have been removed from the match.");
					out.add(String.format("&3Player %s &3has been removed from the match.", player.getPrettyName()));
				}
			}
		}
		if ((currLeader != null && leader != null && !currLeader.getName().equalsIgnoreCase(leader.getName()))
			|| (currLeader != null && leader == null))
		{
			leader = currLeader;
			out.add(String.format(Constants.MSG_NEW_LEADER, leader.getPrettyName(), currLAmount));
		}

		if (currLAmount >= winAmount)
		{
			winner = true;
		}
		return out;
	}

	public void unEquipAll()
	{
		for (String playerName : playerData.keySet())
			this.unEquip(server.getPlayerExact(playerName));
	}

	public void unEquip(IPlayer player)
	{
		if(player == null) return;
		player.getInventory().clear();
	}

	public void end()
	{
		this.teleportAllPlayers(areaHandler.getWaitRoomSpawn());
		this.unEquipAll();
		this.reset();
	}

	public IPlayer getCurrentLeader()
	{
		return leader;
	}

	public ArrayList<IPlayer> getIngamePlayers()
	{
		ArrayList<IPlayer> players = new ArrayList<IPlayer>();
		for (String playerName : playerData.keySet())
			if (!this.playerData.get(playerName))
				players.add(server.getPlayerExact(playerName));
		return players;
	}

	public ArrayList<IPlayer> getPlayersInRange(ILocation location, int range)
	{
		ArrayList<IPlayer> players = new ArrayList<IPlayer>();
		for (String playerName : playerData.keySet())
			if (!this.playerData.get(playerName))
			{
				IPlayer player = server.getPlayerExact(playerName);
				if(player.getLocation().distance(location) < range)
					players.add(player);
			}


		return players;
	}

	private final AreaHandler areaHandler;
	private final RandomItem randomItem;
	private final HashMap<String, Boolean> playerData;
	private final int leaderAmount = -1;
  private final ScoreboardHandler scoreboardHandler;
	private final EquipmentHandler equipmentHandler;
	private final IOutput console;
	private final RunsafeServer server;
	boolean winner = false;
	private IPlayer leader;
	private int winAmount = 0;
}