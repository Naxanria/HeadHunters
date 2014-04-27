package nl.naxanria.headhunters;

import com.google.common.collect.Lists;

import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.framework.minecraft.item.meta.RunsafeMeta;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util
{
	public static int getRandom(int min, int max)
	{
		if (min > max)
		{
			int t = min;
			min = max;
			max = t;
		}

		if (min == max)
		{
			return min;
		}

		return random.nextInt(max - min) + min;
	}

	public static int getRandom(int min, int max, int not)
	{
		if (min > max)
		{
			int t = min;
			min = max;
			max = t;
		}

		if (min == max)
		{
			return min;
		}

		if (max - min == 0 || max - min == 1) return max - min;

		int r = random.nextInt(max - min) + min;
		if (r == not)
            r = getRandom(min, max, not);

		return r;
	}

	public static boolean actPercentage(int percentage)
	{
		return (getRandom(0, 100) < percentage);
	}

	public static int amountMaterial(List<RunsafeMeta> items, RunsafeMeta search)
	{

		int amount = 0;
		for (RunsafeMeta content : items)
			if (content.is(search.getItemType()))
				amount += content.getAmount();



		return amount;

	}

	public static int amountMaterial(IPlayer player, RunsafeMeta search)
	{

		return amountMaterial(player.getInventory().getContents(), search);

	}

	public static String fillZeros(int i)
	{
		String s = i + "";
		if (i < 10) s = "0" + s;
		return s;
	}

	public static boolean arrayListContainsIgnoreCase(ArrayList<String> arrayList, String check)
	{
		for (String toCheck : arrayList)
		{
			if (toCheck.equalsIgnoreCase(check)) return true;
		}
		return false;
	}

	public static void arrayListRemoveIgnoreCase(ArrayList<String> arrayList, String removal)
	{
		ArrayList<String> copy = Lists.newArrayList(arrayList);
		for (String key : copy)
		{
			if (key.equalsIgnoreCase(removal))
			{
				arrayList.remove(key);
				return;
			}
		}
	}

	static final Random random = new Random(System.currentTimeMillis());
}
