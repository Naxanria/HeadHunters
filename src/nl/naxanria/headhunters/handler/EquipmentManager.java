package nl.naxanria.headhunters.handler;

import nl.naxanria.headhunters.database.EquipmentRepository;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

public class EquipmentManager
{
	public EquipmentManager(EquipmentRepository equipmentRepository)
	{
		this.equipmentRepository = equipmentRepository;
		update();
	}

	public void update()
	{
		inventory = equipmentRepository.getEquipment();
	}

	public void equip(RunsafePlayer player)
	{
		player.getInventory().clear();
		if(!inventory.equals(""))
			player.getInventory().unserialize(inventory);
		player.updateInventory();
	}

	public void unEquip(RunsafePlayer player)
	{
		player.getInventory().clear();
		player.getRaw();
	}

	private String inventory = "";
	private final EquipmentRepository equipmentRepository;
}