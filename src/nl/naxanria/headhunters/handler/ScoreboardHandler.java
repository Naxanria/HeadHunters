package nl.naxanria.headhunters.handler;

import no.runsafe.framework.api.player.IPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardHandler {

    public ScoreboardHandler()
    {
			ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
			this.scoreboard = scoreboardManager.getNewScoreboard();
			this.emptyScoreboard = scoreboardManager.getNewScoreboard();
			this.objective = scoreboard.registerNewObjective("Heads", "heads");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName("Heads");

			scoreboardCaption = Bukkit.getOfflinePlayer(ChatColor.GOLD + "Heads needed");

    }

    public void addScoreboard(IPlayer player)
    {
        Player rawPlayer = (Player)player;
        rawPlayer.setScoreboard(scoreboard);
    }

    public void removeScoreBoard(IPlayer player)
    {
        Player rawPlayer = (Player) player;
        scoreboard.resetScores(rawPlayer);
        rawPlayer.setScoreboard(emptyScoreboard);
    }

		public void updateScoreBoardCaption(int amount)
		{
			Score score = objective.getScore(scoreboardCaption);
			score.setScore(amount);
		}

		public void updateScoreboardTimer(String time)
		{
			objective.setDisplayName(ChatColor.AQUA + "Time left:" + ChatColor.WHITE + time + ChatColor.RED + " Heads");
		}

    public void updateScoreboard(IPlayer player, int heads)
    {
			Player rawPlayer = (Player) player;
			Score score = objective.getScore(rawPlayer);
			score.setScore(heads);
    }

    private final Scoreboard scoreboard;
    private final Scoreboard emptyScoreboard;
    private final Objective objective;
		private final OfflinePlayer scoreboardCaption;
}
