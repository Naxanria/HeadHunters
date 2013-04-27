package no.runsafe.headhunters.command;

import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.headhunters.Core;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Naxanria
 * Date: 23-4-13
 * Time: 13:43
 */
public class Join extends PlayerCommand {


    Core core;

    public Join(Core core){
        super("join", "Join a headhunters game", "headhunters.join");
        this.core = core;
    }

    @Override
    public String OnExecute(RunsafePlayer executor, HashMap<String, String> parameters) {

       return this.core.join(executor);


    }
}
