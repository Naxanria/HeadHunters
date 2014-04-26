package nl.naxanria.headhunters.event.custom;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.event.player.RunsafeCustomEvent;

public class CustomHeadhuntersVictory extends RunsafeCustomEvent
{

	public CustomHeadhuntersVictory(IPlayer player)
	{
		super(player, "headhunters.victory");
	}

	@Override
	public Object getData()
	{
		return null;
	}
}
