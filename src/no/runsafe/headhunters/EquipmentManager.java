package no.runsafe.headhunters;

import no.runsafe.framework.minecraft.Enchant;
import no.runsafe.framework.minecraft.Item;
import no.runsafe.framework.minecraft.RunsafeServer;
import no.runsafe.framework.minecraft.inventory.RunsafeInventoryType;
import no.runsafe.framework.minecraft.inventory.RunsafePlayerInventory;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

import java.util.ArrayList;

public class EquipmentManager
{
	public EquipmentManager()
	{
		RunsafePlayerInventory inventory = (RunsafePlayerInventory) RunsafeServer.Instance.createInventory(null, RunsafeInventoryType.PLAYER);
		inventory.setChestplate(Item.Combat.Chestplate.Chainmail.getItem());
		inventory.setLeggings(Item.Combat.Leggings.Chainmail.getItem());
		inventory.setBoots(Item.Combat.Boots.Iron.getItem());
		inventory.setHelmet(Item.Combat.Helmet.Gold.getItem());
		inventory.addItems(Item.Combat.Sword.Iron.getItem());

		RunsafeMeta bow = Item.Combat.Bow.getItem();
		Enchant.InfiniteArrows.power(1).applyTo(bow);
		Enchant.ArrowDamage.power(1).applyTo(bow);
		inventory.addItems(bow);

		inventory.addItems(Item.Combat.Arrow.getItem());
		this.inventory = inventory.serialize();
	}

	public void equip(RunsafePlayer player)
	{
		player.getInventory().unserialize(this.inventory);
		player.updateInventory();
	}

	public void unEquip(RunsafePlayer player)
	{
		player.getInventory().clear();
		player.updateInventory();
	}

	public ArrayList<RunsafeMeta> drops(ArrayList<RunsafeMeta> itemDrop)
	{
		return null;
	}

	private final String inventory;
}