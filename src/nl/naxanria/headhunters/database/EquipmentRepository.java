package nl.naxanria.headhunters.database;

import no.runsafe.framework.api.database.IRow;
import no.runsafe.framework.api.database.Repository;
import no.runsafe.framework.internal.database.jdbc.Database;
import no.runsafe.framework.minecraft.inventory.RunsafePlayerInventory;

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

	@Override
	public HashMap<Integer, List<String>> getSchemaUpdateQueries()
	{

		HashMap<Integer, List<String>> exec = new HashMap<Integer, List<String>>();
		ArrayList<String> sql = new ArrayList<String>();
		sql.add("CREATE TABLE IF NOT EXISTS `headhunters_equipment` (\n" +
				"  `EQUIPMENT` text NOT NULL\n" +
				")");
		exec.put(1, sql);

		return exec;
	}

	public String getEquipment()
	{
		String query = "SELECT EQUIPMENT FROM headhunters_equipment";
		IRow row = database.QueryRow(query);
		if(row == null || row.isEmpty())
			return "";
		return row.String("EQUIPMENT");
	}

	public void setEquipment(String equipment)
	{
		String query = "INSERT INTO headhunters_equipment (`equipment`) VALUES(?);";
		truncate();
		database.Update(query, equipment);
	}

	public void setEquipment(RunsafePlayerInventory equipment)
	{
		setEquipment(equipment.serialize());
	}

	private void truncate()
	{
		String query = "TRUNCATE headhunters_equipment";
		database.Execute(query);
	}


	private final Database database;
}
