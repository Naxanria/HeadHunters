package nl.naxanria.headhunters.command;

import nl.naxanria.headhunters.Constants;
import nl.naxanria.headhunters.Core;
import nl.naxanria.headhunters.database.EquipmentRepository;
import nl.naxanria.headhunters.handler.EquipmentHandler;
import no.runsafe.framework.api.command.argument.IArgumentList;
import no.runsafe.framework.api.command.player.PlayerCommand;
import no.runsafe.framework.api.player.IPlayer;

public class CommandSetEquipment extends PlayerCommand {

	public CommandSetEquipment(EquipmentRepository equipmentRepository, EquipmentHandler equipmentHandler, Core core)
	{
		super("equipment", "Sets the default equipment as the players inventory", "headhunters.admin.equipment");
		this.equipmentRepository = equipmentRepository;
		this.equipmentHandler = equipmentHandler;
		this.core = core;
	}

	@Override
	public String OnExecute(IPlayer executor, IArgumentList parameters)
	{
		if(core.isEnabled())
			return Constants.MSG_COLOR + "Only use this command when headhunters is disabled!";
		equipmentRepository.setEquipment(executor.getInventory());
		equipmentHandler.update();
		return Constants.MSG_COLOR + "Successfully set the standard equipment";
	}

	private final EquipmentRepository equipmentRepository;
	private final EquipmentHandler equipmentHandler;
	private final Core core;

}
