package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Core;
import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.command.ExecutableCommand;
import no.runsafe.framework.api.command.ICommandExecutor;
import nl.naxanria.headhunters.Constants;

import java.util.HashMap;

public class CommandConfig extends ExecutableCommand
{
	public CommandConfig(IConfiguration config, Core core)
	{
		super("config", "Change the config", "headhunters.admin.config", "key", "value");
		captureTail();
		this.config = config;
		this.core = core;
	}

	@Override
	public String OnExecute(ICommandExecutor executor, HashMap<String, String> parameters)
	{
		if (core.isEnabled())
			return Constants.ERROR_COLOR + "Disable headhunters first!";

		String key = parameters.get("key");
		String value = parameters.get("value");

		if (config.getConfigValueAsString(key) == null)
			return String.format("&cKey &f%s&c does not exist", key);

		config.setConfigValue(key, value);
		return String.format("&bSet &f%s&b to &e%s", key, value);
	}

	private final Core core;
	private final IConfiguration config;
}
