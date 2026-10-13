package com.gmail.goosius.siegewar.timeractions;

import com.gmail.goosius.siegewar.enums.SiegeSide;
import com.gmail.goosius.siegewar.events.SiegeCompletedEvent;
import com.gmail.goosius.siegewar.objects.Siege;
import com.gmail.goosius.siegewar.utils.SiegeWarMoneyUtil;
import com.gmail.goosius.siegewar.utils.SiegeWarSiegeCompletionUtil;
import org.bukkit.Bukkit;

/**
 * This class is responsible for processing siege defender wins
 *
 * @author Goosius
 */
public class DefenderWin
{
	/**
	 * This method triggers siege values to be updated for a defender win
	 * SiegeStatus will already have been set
	 *
	 * @param siege the siege
	 */
    public static void defenderWin(Siege siege) {
    	siege.setSiegeWinner(SiegeSide.DEFENDERS);
		SiegeWarSiegeCompletionUtil.setCommonSiegeCompletionValues(siege);
		SiegeWarMoneyUtil.handleWarChest(siege, siege.getTown());
		Bukkit.getPluginManager().callEvent(new SiegeCompletedEvent(siege));
    }

}
