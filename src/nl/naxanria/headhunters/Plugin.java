package nl.naxanria.headhunters;

import nl.naxanria.headhunters.command.*;
import nl.naxanria.headhunters.database.AreaRepository;
import nl.naxanria.headhunters.database.EquipmentRepository;
import nl.naxanria.headhunters.database.WaitRoomRepository;
import nl.naxanria.headhunters.event.*;
import nl.naxanria.headhunters.handler.*;
import no.runsafe.framework.RunsafeConfigurablePlugin;
import no.runsafe.framework.api.command.Command;
import no.runsafe.framework.features.Commands;
import no.runsafe.framework.features.Database;
import no.runsafe.framework.features.Events;
import no.runsafe.framework.features.FrameworkHooks;
import no.runsafe.worldguardbridge.WorldGuardInterface;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void pluginSetup()
	{

		//framework features
		addComponent(Commands.class);
		addComponent(Database.class);
		addComponent(Events.class);
		addComponent(FrameworkHooks.class);

		//worldguard interface
		this.addComponent(getPluginAPI(WorldGuardInterface.class));

		//database
		this.addComponent(AreaRepository.class);
		this.addComponent(WaitRoomRepository.class);
		this.addComponent(EquipmentRepository.class);

		//handlers and managers
    this.addComponent(ScoreboardHandler.class);
		this.addComponent(EquipmentHandler.class);
		this.addComponent(RandomItem.class);
		this.addComponent(AreaHandler.class);
		this.addComponent(VoteHandler.class);
		this.addComponent(PlayerHandler.class);

		//core
		this.addComponent(Core.class);

		// commands
		Command command = new Command("headhunters", "Headhunters plugin commands", null);
		this.addComponent(command);

		command.addSubCommand(this.getInstance(CommandDisable.class));
		command.addSubCommand(this.getInstance(CommandEnable.class));
		command.addSubCommand(this.getInstance(CommandSetCombatArea.class));
		command.addSubCommand(this.getInstance(CommandStart.class));
		command.addSubCommand(this.getInstance(CommandSetSpawn.class));
		command.addSubCommand(this.getInstance(CommandJoin.class));
		command.addSubCommand(this.getInstance(CommandShowIngame.class));
		command.addSubCommand(this.getInstance(CommandLeave.class));
		command.addSubCommand(this.getInstance(CommandStop.class));
		command.addSubCommand(this.getInstance(CommandInfo.class));
		command.addSubCommand(this.getInstance(CommandVote.class));
		command.addSubCommand(this.getInstance(CommandTeleport.class));
		command.addSubCommand(this.getInstance(CommandForceSkip.class));
		command.addSubCommand(this.getInstance(CommandConfig.class));
		command.addSubCommand(this.getInstance(CommandSetWaitroom.class));
		command.addSubCommand(this.getInstance(CommandSetEquipment.class));



		//events
		this.addComponent(PlayerDeath.class);
		this.addComponent(PlayerRespawn.class);
		this.addComponent(PlayerDisconnect.class);
		this.addComponent(PlayerKicked.class);
		this.addComponent(PlayerItemPickUp.class);
		this.addComponent(PlayerRightClick.class);
		this.addComponent(PlayerTeleport.class);

		this.addComponent(BlockBreak.class);
		this.addComponent(BlockPlace.class);
		this.addComponent(DamagePlayer.class);

	}
}
