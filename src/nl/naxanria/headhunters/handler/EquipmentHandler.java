package nl.naxanria.headhunters.handler;

import nl.naxanria.headhunters.database.EquipmentRepository;
import no.runsafe.framework.api.player.IPlayer;

public class EquipmentHandler
{
	public EquipmentHandler(EquipmentRepository equipmentRepository)
	{
		this.equipmentRepository = equipmentRepository;
		update();
	}

	public void update()
	{
		inventory = equipmentRepository.getEquipment();
	}

	public void equip(IPlayer player)
	{
		player.getInventory().clear();
		if(!inventory.equals(""))
			player.getInventory().unserialize(inventory);
		player.updateInventory();
	}

	public void unEquip(IPlayer player)
	{
		player.getInventory().clear();
	}

	private String inventory = "";
	private final EquipmentRepository equipmentRepository;
}