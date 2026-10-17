package com.gmail.goosius.siegewar.objects;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.gmail.goosius.siegewar.Messaging;
import com.gmail.goosius.siegewar.SiegeController;
import com.gmail.goosius.siegewar.SiegeTypeRegistry;
import com.gmail.goosius.siegewar.api.SiegeTypeDefinition;
import com.gmail.goosius.siegewar.enums.SiegeType;
import com.gmail.goosius.siegewar.events.PreSiegeWarStartEvent;
import com.gmail.goosius.siegewar.settings.SiegeWarSettings;
import com.gmail.goosius.siegewar.utils.SiegeWarMoneyUtil;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Government;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.util.TimeTools;

public class SiegeCamp {
	private final Player player;
	private final Block bannerBlock;
	private SiegeType siegeType;
	private final SiegeType originalSiegeType;
	private NamespacedKey customSiegeTypeId;
	private Map<String, String> customData = Map.of();
	private Integer battleSessionsRequired;
	private boolean skipSiegeCamp;
	private boolean skipSiegeValidation;
	private boolean legacyPlayerEventsEnabled = true;
	private final Town targetTown;
	private final Government attacker;
	private final Government defender;
	private final Town townOfSiegeStarter;
	private final TownBlock townBlock;
	private int attackerPoints = 0;
	private final long endTime;

	public SiegeCamp(Player player, Block bannerBlock, SiegeType siegeType, Town targetTown, Government attacker,
			Government defender, Town townOfSiegeStarter,TownBlock townBlock) {
		this.player = player;
		this.bannerBlock = bannerBlock;
		this.siegeType = siegeType;
		this.originalSiegeType = siegeType;
		this.targetTown = targetTown;
		this.attacker = attacker;
		this.defender = defender;
		this.townOfSiegeStarter = townOfSiegeStarter;
		this.townBlock = townBlock;
		this.endTime = System.currentTimeMillis() + TimeTools.getMillis(SiegeWarSettings.getSiegeCampDurationInMinutes() + "m");
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the bannerBlock
	 */
	public Block getBannerBlock() {
		return bannerBlock;
	}

	/**
	 * @return the siegeType
	 */
	public SiegeType getSiegeType() {
		return siegeType;
	}

	public NamespacedKey getCustomSiegeTypeId() {
		return customSiegeTypeId;
	}

	public void setCustomSiegeTypeId(NamespacedKey id) {
		SiegeTypeDefinition definition = id == null ? null : SiegeTypeRegistry.getDefinition(id);
		if (id != null && definition == null)
			throw new IllegalArgumentException("Unregistered siege type: " + id);
		customSiegeTypeId = id;
		siegeType = definition == null ? originalSiegeType : definition.getBaseType();
	}

	public NamespacedKey getSiegeTypeId() {
		return customSiegeTypeId == null ? new NamespacedKey("siegewar", siegeType.toLowerCase()) : customSiegeTypeId;
	}

	public Map<String, String> getCustomData() {
		return customData;
	}

	public void setCustomData(Map<String, String> data) {
		customData = data == null ? Map.of() : Map.copyOf(data);
	}

	public Integer getBattleSessionsRequired() {
		return battleSessionsRequired;
	}

	public void setBattleSessionsRequired(Integer sessions) {
		if (sessions != null && sessions <= 0)
			throw new IllegalArgumentException("Battle sessions required must be positive");
		battleSessionsRequired = sessions;
	}

	public boolean isSkipSiegeCamp() {
		return skipSiegeCamp;
	}

	public void setSkipSiegeCamp(boolean skip) {
		skipSiegeCamp = skip;
	}

	public boolean isSkipSiegeValidation() {
		return customSiegeTypeId != null && skipSiegeValidation;
	}

	public void setSkipSiegeValidation(boolean skip) {
		skipSiegeValidation = skip;
	}

	public boolean isLegacyPlayerEventsEnabled() {
		return legacyPlayerEventsEnabled;
	}

	public void setLegacyPlayerEventsEnabled(boolean enabled) {
		legacyPlayerEventsEnabled = enabled;
	}

	/**
	 * @return the targetTown
	 */
	public Town getTargetTown() {
		return targetTown;
	}

	/**
	 * @return the attacker
	 */
	public Government getAttacker() {
		return attacker;
	}

	/**
	 * @return the defender
	 */
	public Government getDefender() {
		return defender;
	}

	/**
	 * @return the townOfSiegeStarter
	 */
	public Town getTownOfSiegeStarter() {
		return townOfSiegeStarter;
	}

	/**
	 * @return the townBlock
	 */
	public TownBlock getTownBlock() {
		return townBlock;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @return the attackerPoints
	 */
	public int getAttackerPoints() {
		return attackerPoints;
	}

	/**
	 * @param attackerPoints the attackerPoints to set
	 */
	public void setAttackerPoints(int attackerPoints) {
		this.attackerPoints = attackerPoints;
	}

	/**
	 * Starts the Siege after the success of the SiegeCamp.
	 */
	public void startSiege() {
		SiegeTypeDefinition definition = customSiegeTypeId == null ? null : SiegeTypeRegistry.getDefinition(customSiegeTypeId);
		if (customSiegeTypeId != null && (definition == null || !SiegeTypeRegistry.isAvailable(customSiegeTypeId)
				|| definition.getBaseType() != siegeType)) {
			if (player != null)
				Messaging.sendErrorMsg(player, Translatable.of("msg_err_custom_siege_type_unavailable", customSiegeTypeId));
			return;
		}
		if (SiegeController.hasActiveSiege(targetTown))
			return;
		//Test that the siege starter can pay the siege start cost
		if (TownyEconomyHandler.isActive()) {
			double siegeStartCost = SiegeWarMoneyUtil.calculateTotalSiegeStartCost(targetTown);
			if (siegeType.equals(SiegeType.CONQUEST) && !attacker.getAccount().canPayFromHoldings(siegeStartCost)) {
				TownyMessaging.sendPrefixedNationMessage((Nation)attacker, Translatable.of("msg_err_your_nation_cannot_afford_to_siege_for_x", TownyEconomyHandler.getFormattedBalance(siegeStartCost)));
				return;
			} else if (siegeType.equals(SiegeType.REVOLT) && !targetTown.getAccount().canPayFromHoldings(siegeStartCost)) {
				TownyMessaging.sendPrefixedTownMessage(targetTown, Translatable.of("msg_err_your_town_cannot_afford_to_siege_for_x", TownyEconomyHandler.getFormattedBalance(siegeStartCost)));
				return;
			}
		}

		// Call event
		if (legacyPlayerEventsEnabled) {
			PreSiegeWarStartEvent event = new PreSiegeWarStartEvent(this);
			Bukkit.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				Messaging.sendErrorMsg(player, event.getCancellationMessage());
				return;
			}
		}
		if (customSiegeTypeId != null && !SiegeTypeRegistry.isAvailable(customSiegeTypeId))
			return;
		if (SiegeController.hasActiveSiege(targetTown))
			return;
		SiegeController.startSiege(this);
	}

}
