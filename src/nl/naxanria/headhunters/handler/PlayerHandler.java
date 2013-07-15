package nl.naxanria.headhunters.handler;

import nl.naxanria.headhunters.Constants;
import nl.naxanria.headhunters.Util;
import no.runsafe.framework.minecraft.Buff;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.RunsafeLocation;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;
import no.runsafe.framework.minecraft.player.RunsafePlayer;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerHandler
{
	public PlayerHandler(EquipmentManager manager, AreaHandler areaHandler, ScoreboardHandler scoreboardHandler)
	{
		//playerData = new HashMap<String, HashMap<String, Object>>();
		playerData = new HashMap<String, Boolean>();
		this.equipmentManager = manager;
		this.areaHandler = areaHandler;
        this.scoreboardHandler = scoreboardHandler;
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

	public void remove(RunsafePlayer player)
	{
		if (isIngame(player))
		{
			playerData.put(player.getName(), true);
            scoreboardHandler.removeScoreBoard(player);

			unEquip(player);
			//Todo: add dropping all usable items
			RunsafeMeta heads = Item.Decoration.Head.Human.getItem();
			heads.setAmount(Util.amountMaterial(player, heads));
			player.getWorld().dropItem(player.getEyeLocation(), heads);
			player.getInventory().clear();
			player.teleport(areaHandler.getWaitRoomSpawn());

			ArrayList<RunsafePlayer> ingame = getIngamePlayers();

			if (ingame.size() == 1)
			{
				winner = true;
				leader = ingame.get(0);
			}
		}
	}

	public boolean isIngame(RunsafePlayer player)
	{
		String playerName = player.getName();
		return areaHandler.isInGameWorld(player) && playerData.containsKey(playerName) && !playerData.get(playerName);
	}


	public void addPlayer(RunsafePlayer player)
	{
		playerData.put(player.getName(), false);
        scoreboardHandler.addScoreboard(player);
	}

	public void addPlayers(ArrayList<RunsafePlayer> players)
	{
		for (RunsafePlayer player : players) addPlayer(player);
	}

	public void teleportAllPlayers(RunsafeLocation location)
	{
		for (String playerName : playerData.keySet())
			if (!playerData.get(playerName))
				RunsafeServer.Instance.getPlayerExact(playerName).teleport(location);
	}

	public void teleport()
	{
		for (String playerName : playerData.keySet())
			if (!this.playerData.get(playerName))
				RunsafeServer.Instance.getPlayerExact(playerName).teleport(areaHandler.getSafeLocation());
	}

	public void start(ArrayList<RunsafePlayer> players)
	{
		addPlayers(players);
		unEquipAll();
		setUpPlayers();
		teleport();
		winAmount = (int) ((players.size() / 2) + players.size() * 3.5);
	}

	public void setUpPlayers()
	{
		for (String playerName : playerData.keySet())
			this.setUpPlayer(RunsafeServer.Instance.getPlayerExact(playerName));
	}

	public void setUpPlayer(RunsafePlayer player)
	{
		player.setGameMode(GameMode.SURVIVAL);
		equipmentManager.equip(player);
		player.setSaturation(10f);
		player.setHealth(20);
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

  public void resetScoreboard()
    {
        for(String playerName : playerData.keySet())
            scoreboardHandler.removeScoreBoard(RunsafeServer.Instance.getPlayerExact(playerName));
  }

	public ArrayList<String> tick()
	{
		ArrayList<String> out = new ArrayList<String>();
		int currLAmount = 0;
		RunsafePlayer currLeader = null;
		for (String playerName : playerData.keySet())
		{
			if (!this.playerData.get(playerName))
			{
				RunsafePlayer player = RunsafeServer.Instance.getPlayerExact(playerName);
				int amount = Util.amountMaterial(player, Item.Decoration.Head.Human.getItem());
				if (amount != 0 && amount > leaderAmount && amount > currLAmount)
				{
					currLeader = player;
					currLAmount = amount;
				}
        scoreboardHandler.updateScoreboard(player, amount);
				player.setSaturation(10f);
				if (!player.getWorld().getName().equalsIgnoreCase(getWorldName()))
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
			this.unEquip(RunsafeServer.Instance.getPlayerExact(playerName));
	}

	public void unEquip(RunsafePlayer player)
	{
		player.getInventory().clear();
	}

	public void end()
	{

		this.teleportAllPlayers(areaHandler.getWaitRoomSpawn());
		this.unEquipAll();
		this.reset();
	}

	public RunsafePlayer getCurrentLeader()
	{
		return leader;
	}

	public ArrayList<RunsafePlayer> getIngamePlayers()
	{
		ArrayList<RunsafePlayer> players = new ArrayList<RunsafePlayer>();
		for (String playerName : playerData.keySet())
			if (!this.playerData.get(playerName))
				players.add(RunsafeServer.Instance.getPlayerExact(playerName));
		return players;
	}

	public ArrayList<RunsafePlayer> getIngamePlayers(RunsafeLocation location, int range)
	{
		ArrayList<RunsafePlayer> players = new ArrayList<RunsafePlayer>();
		for (String playerName : playerData.keySet())
        {
            RunsafePlayer player = RunsafeServer.Instance.getPlayerExact(playerName);
			if (!this.playerData.get(playerName) && location.distance(player.getLocation()) < range)
				players.add(player);
        }
		return players;
	}

	private final AreaHandler areaHandler;
	//private final HashMap<String, HashMap<String, Object>> playerData;
	private final HashMap<String, Boolean> playerData;
	private final int leaderAmount = -1;
  private final ScoreboardHandler scoreboardHandler;
	private final EquipmentManager equipmentManager;
	Boolean winner = false;
	private RunsafePlayer leader;
	private int winAmount = 0;
}