package no.runsafe.headhunters.command;

import no.runsafe.framework.command.player.PlayerCommand;
import no.runsafe.framework.output.ChatColour;
import no.runsafe.framework.server.player.RunsafePlayer;
import no.runsafe.headhunters.Core;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Naxanria
 * Date: 20-4-13
 * Time: 18:31
 */
public class disable extends PlayerCommand {

    Core core;

    public disable(Core core){
        super("disable", "Disables the plugin", "headhunters.enable");
        this.core = core;
    }


    @Override
    public String OnExecute(RunsafePlayer executor, HashMap<String, String> parameters) {

        this.core.disable();

        return "Headhunters " + ChatColour.RED + "disabled";
    }
}