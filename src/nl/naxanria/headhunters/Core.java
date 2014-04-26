package nl.naxanria.headhunters;


import nl.naxanria.headhunters.database.WaitRoomRepository;
import nl.naxanria.headhunters.event.custom.CustomHeadhuntersVictory;
import nl.naxanria.headhunters.handler.AreaHandler;
import nl.naxanria.headhunters.handler.PlayerHandler;
import nl.naxanria.headhunters.handler.ScoreboardHandler;
import nl.naxanria.headhunters.handler.VoteHandler;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.event.plugin.IPluginEnabled;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.internal.extension.RunsafeServer;
import no.runsafe.framework.minecraft.Item;

import no.runsafe.framework.text.ChatColour;

import nl.naxanria.headhunters.database.AreaRepository;
import no.runsafe.worldguardbridge.WorldGuardInterface;
import org.bukkit.GameMode;

import java.util.ArrayList;

public class Core implements IConfigurationChanged, IPluginEnabled
{

    public Core(IOutput console, IScheduler scheduler, RunsafeServer server, VoteHandler voteHandler,
							PlayerHandler playerHandler, AreaHandler areaHandler, WorldGuardInterface worldGuardInterface,
							AreaRepository areaRepository, WaitRoomRepository waitRoomRepository, ScoreboardHandler scoreboardHandler)
	{
		this.console = console;
		this.server = server;
		this.playerHandler = playerHandler;
		this.voteHandler = voteHandler;
		this.areaHandler = areaHandler;
		this.areaRepository = areaRepository;
		this.gamestarted = false;
   	this.waitRoomRepository = waitRoomRepository;
		this.scoreboardHandler = scoreboardHandler;

		SimpleArea.setWorldGuardInterface(worldGuardInterface);

		scheduler.startSyncRepeatingTask(
			new Runnable()
			{
				@Override
				public void run()
				{
					tick();
				}
			}, 1, 1
		);
	}

	public boolean enable()
	{
		console.broadcastColoured("enabling");
		this.enabled = true;
		if (loadAreas() != 0) return false;
		if (areaHandler.getAmountLoadedAreas() == 0)
			return false;
        if (!initWaitRoom())
            return false;
		this.config.setConfigValue("enabled", true);
		this.config.save();

		end();
		return true;
	}

	private boolean initWaitRoom()
	{
		return areaHandler.initWaitRoom(waitRoomRepository.getWaitRoom());
	}

	private int loadAreas()
	{
		console.broadcastColoured("loading areas");
		ArrayList<String> areas = areaRepository.getAreas();

		if (areas.isEmpty())
			return 1;
		areaHandler.loadAreas(areas);
        console.broadcastColoured("loaded " + areas.size() + "areas");

		return 0;
	}

	public void disable()
	{
		console.broadcastColoured("disabling");
		if (this.gamestarted)
			this.end();

		gamestarted = false;
		this.config.setConfigValue("enabled", false);
		this.config.save();
		this.enabled = false;
	}

	public boolean getGamestarted()
	{
		return gamestarted;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public int getTimeToStart()
	{
		return countdownToStart;
	}

	public int getTimeToEnd()
	{
		return countdownToEnd;
	}

	public String startInTime(Integer time)
	{
		if (!this.enabled)
			return Constants.MSG_DISABLED;

		if (gamestarted)
			return "Game already started";

		countdownToStart = time <= 0 ? 1 : time;
		return Constants.MSG_COLOR + "Game will start in " + ChatColour.WHITE + countdownToStart + Constants.MSG_COLOR + " seconds";
	}

	public void start()
	{
		console.broadcastColoured("starting game");
		if (areaHandler.getAmountLoadedAreas() == 0)
		{
			console.broadcastColoured("Can not start headhunters game; There are no areas");
			disable();
			return;
		}

		ArrayList<IPlayer> players = areaHandler.getWaitRoomPlayers(GameMode.SURVIVAL);
		int minPlayers = 2;
		if (players.size() < minPlayers)
		{
			this.resetWaittime();
			sendMessage(String.format(Constants.MSG_NOT_ENOUGH_PLAYERS, players.size(), minPlayers), players);
			return;
		}

		this.gamestarted = true;
		areaHandler.setNextAsCurrentArea();
		playerHandler.start(players);
		sendMessage(String.format("Map: &f%s", areaHandler.getAreaName(areaHandler.getCurrentArea())), players);
		sendMessage(String.format(Constants.MSG_START_MESSAGE, playerHandler.getWinAmount()), players);
	}

	public void end()
	{
		console.broadcastColoured("stopping game");
		playerHandler.end();

		areaHandler.removeEntities();
		areaHandler.randomNextArea();
		gamestarted = false;

		resetWaittime();
		resetRuntime();
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		console.broadcastColoured("loading config...");
		this.config = configuration;
		this.enabled = config.getConfigValueAsBoolean("enabled");
		this.countdownToStart = config.getConfigValueAsInt("waittime");
		this.countdownToEnd = config.getConfigValueAsInt("runtime");
		this.voteHandler.setMinPercentage(config.getConfigValueAsInt("vote.min-percent"));
		this.voteHandler.setMinVotes(config.getConfigValueAsInt("vote.min-votes"));
		console.broadcastColoured("finished");
	}

	private void resetWaittime()
	{
		this.countdownToStart = config.getConfigValueAsInt("waittime");
	}

	private void resetRuntime()
	{
		this.countdownToEnd = config.getConfigValueAsInt("runtime");
	}

	public void sendMessage(String msg, ArrayList<IPlayer> players)
	{
		for (IPlayer player : players)
			player.sendColouredMessage(msg);
	}

	public void tick()
	{
		if (enabled)
		{
			if (!this.gamestarted)
			{
				ArrayList<IPlayer> waitingRoomPlayers = areaHandler.getWaitRoomPlayers(GameMode.SURVIVAL);
				voteHandler.setCanVote(true);
				if (voteHandler.votePass(waitingRoomPlayers.size()))
				{
					sendMessage(String.format(Constants.MSG_NEW_NEXT_MAP_VOTED, areaHandler.getAreaName(areaHandler.randomNextArea())), waitingRoomPlayers);
					voteHandler.resetVotes();
				}
				if (waitingRoomPlayers.size() > 1)
				{
					if (countdownToStart % 300 == 0)
					{ //every 5 minutes
						server.broadcastMessage(String.format(Constants.MSG_GAME_START_IN, (countdownToStart / 300) * 5, "minutes"));
					}
					else
					{
						switch (countdownToStart)
						{
							case 180:
								sendMessage(String.format(Constants.MSG_GAME_START_IN, 3, "minutes"), waitingRoomPlayers);
								break;
							case 120:
								sendMessage(String.format(Constants.MSG_GAME_START_IN, 2, "minutes"), waitingRoomPlayers);
								break;
							case 60:
								sendMessage(String.format(Constants.MSG_GAME_START_IN, 1, "minute"), waitingRoomPlayers);
								break;
							case 30:
								sendMessage(String.format(Constants.MSG_GAME_START_IN, 30, "seconds"), waitingRoomPlayers);
								break;
							case 20:
								sendMessage(String.format(Constants.MSG_GAME_START_IN, 20, "seconds"), waitingRoomPlayers);
								break;
							case 10:
								sendMessage(String.format(Constants.MSG_GAME_START_IN, 10, "seconds"), waitingRoomPlayers);
								break;
							case 5:
								sendMessage(String.format(Constants.MSG_GAME_START_IN, 5, "seconds"), waitingRoomPlayers);
								break;
						}
					}

					countdownToStart--;
				}
				else
				{
					resetWaittime();
				}
				if (countdownToStart <= 0)
				{
					start();
					voteHandler.setCanVote(false);
					voteHandler.resetVotes();
				}
				else
					//moving all players not in creative mode into the waitroom
					for (IPlayer p : areaHandler.getWorld().getPlayers())
						if (p.getGameMode() != GameMode.CREATIVE && !areaHandler.isInWaitRoom(p))
							p.teleport(areaHandler.getWaitRoomSpawn());

			}
			else
			{
				for (String s : playerHandler.tick())
					sendMessage(s, playerHandler.getIngamePlayers());

				if (playerHandler.isWinner())
				{
					winner();
					return;
				}

				if (countdownToEnd % 300 == 0)
				{
					sendMessage(String.format(Constants.MSG_TIME_REMAINING, (countdownToEnd / 300) * 5, "minutes"), playerHandler.getIngamePlayers());
				}
				else
				{

					switch (countdownToEnd)
					{
						case 180:
							sendMessage(String.format(Constants.MSG_TIME_REMAINING, 3, "minutes"), playerHandler.getIngamePlayers());
							break;
						case 120:
							sendMessage(String.format(Constants.MSG_TIME_REMAINING, 2, "minutes"), playerHandler.getIngamePlayers());
							break;
						case 60:
							sendMessage(String.format(Constants.MSG_TIME_REMAINING, 1, "minute"), playerHandler.getIngamePlayers());
							break;
						case 30:
							sendMessage(String.format(Constants.MSG_TIME_REMAINING, 30, "seconds"), playerHandler.getIngamePlayers());
							break;
						case 20:
							sendMessage(String.format(Constants.MSG_TIME_REMAINING, 20, "seconds"), playerHandler.getIngamePlayers());
							break;
						case 10:
							sendMessage(String.format(Constants.MSG_TIME_REMAINING, 10, "seconds"), playerHandler.getIngamePlayers());
							break;
						case 5:
							sendMessage(String.format(Constants.MSG_TIME_REMAINING, 5, "seconds"), playerHandler.getIngamePlayers());
							break;
					}
				}

				int min = countdownToEnd / 60;
				int sec = countdownToEnd % 60;
				scoreboardHandler.updateScoreboardTimer(String.format("%s:%s", Util.fillZeros(min), Util.fillZeros(sec)));

				this.countdownToEnd--;
				if (countdownToEnd <= 0)
				{
					winner();
					return;
				}

				// checking all players in world that are not creative mode, move them to the waiting room if not in game
				for (IPlayer p : areaHandler.getWorld().getPlayers())
					if (!playerHandler.isIngame(p) && p.getGameMode() != GameMode.CREATIVE && !areaHandler.isInWaitRoom(p))
						p.teleport(areaHandler.getWaitRoomSpawn());
			}
		}
	}

	private void winner()
	{
		console.broadcastColoured("checking for winner");
		IPlayer winPlayer = playerHandler.getCurrentLeader();
		if (winPlayer != null)
		{
			server.broadcastMessage(String.format(Constants.GAME_WON, winPlayer.getPrettyName()));
			new CustomHeadhuntersVictory(winPlayer).Fire();
		}
		end();
		playerHandler.reset();
	}

	public String join(IPlayer executor)
	{
		if (this.enabled)
		{
			if (!playerHandler.isIngame(executor))
			{
				executor.teleport(areaHandler.getWaitRoomSpawn());
				executor.setGameMode(GameMode.SURVIVAL);
				return null;
			}
			return Constants.ERROR_COLOR + "You are already in game!";
		}

		return Constants.MSG_COLOR + "headhunters is not enabled";
	}

	public void stop(IPlayer executor)
	{
		if (gamestarted)
		{
			winner();
			sendMessage(String.format(Constants.MSG_GAME_STOPPED, executor.getPrettyName()), playerHandler.getIngamePlayers());
		}
	}

	public int amountHeads(IPlayer player)
	{
		return Util.amountMaterial(player, Item.Decoration.Head.Human.getItem());
	}

	@Override
	public void OnPluginEnabled()
	{
		console.broadcastColoured("enabling sequence");
		if (this.enabled)
			if (!enable())
				disable();

		//console.broadcastColoured((enabled) ? ConsoleColour.GREEN + "Enabled" : ConsoleColour.RED + "Disabled");
		console.broadcastColoured(String.format("loaded %d areas", areaHandler.getAmountLoadedAreas()));
	}

	private final IOutput console;
	private final RunsafeServer server;
	private final VoteHandler voteHandler;
	private final PlayerHandler playerHandler;
	private final AreaHandler areaHandler;
	private final AreaRepository areaRepository;
	private final WaitRoomRepository waitRoomRepository;
	private final ScoreboardHandler scoreboardHandler;
	private boolean gamestarted = false;
	private boolean enabled = false;
	private IConfiguration config;
	private int countdownToStart = -1;
	private int countdownToEnd = -1;
}
