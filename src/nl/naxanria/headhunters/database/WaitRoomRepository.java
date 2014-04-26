package nl.naxanria.headhunters.database;

import nl.naxanria.headhunters.SimpleArea;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.database.*;
import no.runsafe.framework.internal.database.Set;

public class WaitRoomRepository extends Repository
{

	public WaitRoomRepository(IDatabase database, IOutput console)
	{
		this.database = database;
		this.console = console;
	}

	@Override
	public String getTableName()
	{
		return "headhunters_waitroom";
	}

	@Override
	public ISchemaUpdate getSchemaUpdateQueries()
	{
		console.broadcastColoured("WaitingRoomRepository - schema updates");
		SchemaUpdate schemaUpdate = new SchemaUpdate();
		schemaUpdate.addQueries(
				"CREATE TABLE IF NOT EXISTS `headhunters_waitroom` (\n" +
				"  `AREANAME` varchar(64) NOT NULL\n" +
				")"
				,
				"ALTER TABLE  `headhunters_waitroom` ADD  `WORLD` VARCHAR( 128 ) NOT NULL"
		);

		return schemaUpdate;
	}

	public SimpleArea getWaitRoom()
	{
		String query = "SELECT areaname,world FROM headhunters_waitroom";
		IRow row = database.queryRow(query);
		if (row == null || row == Set.Empty)
            return null;
        return new SimpleArea(row.World("WORLD"), row.String("AREANAME"));
	}

	private void delWaitRoom()
	{
		String query = "TRUNCATE TABLE headhunters_waitroom";
		database.execute(query);
	}

	public void setWaitRoom(String name, String world)
	{
		String query = String.format("INSERT INTO headhunters_waitroom (`AREANAME`, `WORLD`) VALUES ('%s', '%s');", name, world);
		delWaitRoom();
		database.update(query);
	}

	private final IDatabase database;
	private final IOutput console;
}
