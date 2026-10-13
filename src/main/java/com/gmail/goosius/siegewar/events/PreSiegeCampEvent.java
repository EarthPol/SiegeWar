package com.gmail.goosius.siegewar.events;

import com.gmail.goosius.siegewar.enums.SiegeType;
import com.gmail.goosius.siegewar.objects.SiegeCamp;
import com.gmail.goosius.siegewar.settings.SiegeWarSettings;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.Translatable;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A cancellable event that marks the beginning of a {@link SiegeCamp}.
 * 
 * SiegeCamps precede Sieges, acting as a minigame of sorts, in which the
 * Siege-starter must gain an amount of points:
 * {@link SiegeWarSettings#getSiegeCampPointsForSuccess()}, in order for the
 * Siege to begin in earnest. This gives towns an opportunity to disrupt two
 * friendly towns from acting out a mock-siege, in order to get around being
 * sieged by actual enemies.
 * 
 * @author LlmDl
 * @since 0.6.5
 */
public class PreSiegeCampEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final SiegeCamp siegeCamp;
    private boolean isCancelled;
    private Translatable cancellationMessage = Translatable.of("msg_siege_camp_cancelled");

    public PreSiegeCampEvent(SiegeCamp camp){
        this.siegeCamp = camp;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Town getTownOfSiegeStarter() {
        return siegeCamp.getTownOfSiegeStarter();
    }

    public Nation getNation() {
        return (Nation) siegeCamp.getAttacker();
    }

    public SiegeCamp getCamp() {
        return siegeCamp;
    }

    public Player getSiegeStarter() {
        return siegeCamp.getPlayer();
    }

    public NamespacedKey getSiegeTypeId() {
        return siegeCamp.getSiegeTypeId();
    }

    public NamespacedKey getCustomSiegeTypeId() {
        return siegeCamp.getCustomSiegeTypeId();
    }

    public void setCustomSiegeTypeId(NamespacedKey id) {
        siegeCamp.setCustomSiegeTypeId(id);
    }

    public boolean isSkipSiegeCamp() {
        return siegeCamp.isSkipSiegeCamp();
    }

    public void setSkipSiegeCamp(boolean skip) {
        siegeCamp.setSkipSiegeCamp(skip);
    }

    public Block getFlag() {
        return siegeCamp.getBannerBlock();
    }

    public TownBlock getTownBlock() {
        return siegeCamp.getTownBlock();
    }

    public Town getTargetTown() {
        return siegeCamp.getTargetTown();
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }

    public String getCancellationMsg() {
        return cancellationMessage == null ? null : cancellationMessage.defaultLocale();
    }

    public void setCancellationMsg(String cancellationMsg) {
        cancellationMessage = cancellationMsg == null ? null : Translatable.literal(cancellationMsg);
    }

    public Translatable getCancellationMessage() {
        return cancellationMessage;
    }

    public void setCancellationMessage(Translatable message) {
        cancellationMessage = message;
    }

    public SiegeType getSiegeType() {
        return siegeCamp.getSiegeType();
    }
}
