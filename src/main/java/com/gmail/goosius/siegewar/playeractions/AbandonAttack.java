package com.gmail.goosius.siegewar.playeractions;

import com.gmail.goosius.siegewar.Messaging;
import com.gmail.goosius.siegewar.SiegeController;
import com.gmail.goosius.siegewar.enums.SiegeStatus;
import com.gmail.goosius.siegewar.enums.SiegeSide;
import com.gmail.goosius.siegewar.enums.SiegeWarPermissionNodes;
import com.gmail.goosius.siegewar.objects.Siege;
import com.gmail.goosius.siegewar.settings.SiegeWarSettings;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Translatable;
import org.bukkit.entity.Player;

/**
 * This class is responsible for processing requests to Abandon siege attacks
 *
 * @author Goosius
 */
public class AbandonAttack {

	/**
	 * At this point we know the player is a member of the attacking army
	 *
	 * @param player player
	 * @param siege siege
	 * @throws TownyException
	 */
	public static void processAbandonAttackRequest(Player player, Siege siege) throws TownyException {
		if (!siege.isSiegeTypeAvailable())
			throw new TownyException(Translatable.of("msg_err_custom_siege_type_unavailable", siege.getSiegeTypeId()));

		if(!SiegeWarSettings.getWarSiegeAbandonEnabled())
			throw new TownyException(Translatable.of("msg_err_abandoning_attack_disabled_in_the_config"));

		if (!TownyUniverse.getInstance().getPermissionSource().testPermission(player, SiegeWarPermissionNodes.SIEGEWAR_NATION_SIEGE_ABANDON.getNode()))
			throw new TownyException(Translatable.of("msg_err_action_disable_missing_node", SiegeWarPermissionNodes.SIEGEWAR_NATION_SIEGE_ABANDON.getNode()));

		Confirmation
			.runOnAccept(()-> abandonAttack(siege))
			.runOnCancel(()-> Messaging.sendMsg(player, Translatable.of("msg_abandon_action_cancelled")))
			.sendTo(player);
	}

    public static void abandonAttack(Siege siege) {
		if (!siege.isSiegeTypeAvailable())
			return;

		//Send global message
		//Do abandon
		if(!siege.hasCompletedAllBattleSessions()) {
			Messaging.sendGlobalMessage(getAbandonMessage(siege));
			//Pending abandon
			siege.setStatus(SiegeStatus.PENDING_ATTACKER_ABANDON);
			SiegeController.saveSiege(siege);
		} else {
			//Immediate abandon
			SiegeController.completeSiegeWithStatus(siege, SiegeSide.DEFENDERS,
					SiegeStatus.ATTACKER_ABANDON, getAbandonMessage(siege));
		}
	}

	private static Translatable getAbandonMessage(Siege siege) {
		if (siege.isCustomSiegeType())
			return Translatable.of(siege.hasCompletedAllBattleSessions()
					? "msg_custom_siege_attacker_abandon_confirmed" : "msg_custom_siege_attacker_abandon",
					siege.getTranslatedSiegeTypeName(), siege.getTown().getName(), siege.getAttacker().getName());
		Translatable message;
		String key;
		if (!siege.hasCompletedAllBattleSessions()) {
			//Pending
			key = String.format("msg_%s_siege_attacker_abandon", siege.getSiegeType().toLowerCase());
			message = Translatable.of(key, siege.getTown().getName(), siege.getAttacker().getName());
		} else {
			//Base message
			key = String.format("msg_%s_siege_attacker_abandon_confirmed", siege.getSiegeType().toLowerCase());
			message = Translatable.of(key, siege.getTown().getName());
			//Standard effects
			key= String.format("msg_%s_siege_defender_win_result", siege.getSiegeType().toLowerCase());
			message.append(Translatable.of(key));
		}

		return message;
	}
}
