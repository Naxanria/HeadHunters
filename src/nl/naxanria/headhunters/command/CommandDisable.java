package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Core;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import no.runsafe.framework.text.ChatColour;

import java.util.HashMap;
import java.util.Map;

public class CommandDisable extends ExecutableCommand
{
	public CommandDisable(Core core)
	{
		super("disable", "Disables the plugin", "headhunters.admin.disable");
		this.core = core;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, Map<String, String> parameters)
	{
		this.core.disable();
		return "Headhunters " + ChatColour.RED + "disabled";
	}

	private final Core core;

}