package nl.naxanria.headhunters.database;

import no.runsafe.framework.api.database.*;
import no.runsafe.framework.api.event.inventory.IInventory;
import no.runsafe.framework.internal.database.jdbc.Database;
import no.runsafe.framework.minecraft.inventory.RunsafeInventory;
import no.runsafe.framework.minecraft.inventory.RunsafePlayerInventory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EquipmentRepository extends Repository
{

	public EquipmentRepository(Database database)
	{
		this.database = database;

	}

	@Override
	public String getTableName()
	{
		return "headhunters_equipment";
	}

	@Nonnull
	@Override
	public ISchemaUpdate getSchemaUpdateQueries()
	{
		SchemaUpdate schemaUpdate = new SchemaUpdate();
		schemaUpdate.addQueries("CREATE TABLE IF NOT EXISTS `headhunters_equipment` (\n" +
				"  `EQUIPMENT` text NOT NULL\n" +
				")");

		return schemaUpdate;
	}

	public String getEquipment()
	{
		String query = "SELECT EQUIPMENT FROM headhunters_equipment";
		IRow row = database.queryRow(query);
		if(row == null || row.isEmpty())
			return "";
		return row.String("EQUIPMENT");
	}

	public void setEquipment(String equipment)
	{
		String query = "INSERT INTO headhunters_equipment (`equipment`) VALUES(?);";
		truncate();
		database.update(query, equipment);
	}

	public void setEquipment(RunsafeInventory equipment)
	{
		setEquipment(equipment.serialize());
	}

	private void truncate()
	{
		String query = "TRUNCATE headhunters_equipment";
		database.execute(query);
	}


	private final Database database;
}
