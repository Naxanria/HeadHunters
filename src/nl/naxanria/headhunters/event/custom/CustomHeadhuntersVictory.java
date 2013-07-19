package nl.naxanria.headhunters.event.custom;

import no.runsafe.framework.minecraft.event.player.RunsafeCustomEvent;
import no.runsafe.framework.minecraft.player.RunsafePlayer;

public class CustomHeadhuntersVictory extends RunsafeCustomEvent {

	public CustomHeadhuntersVictory(RunsafePlayer player)
	{
		super(player, "headhunters.victory");
	}

	@Override
	public Object getData() {
		return null;
	}
}
