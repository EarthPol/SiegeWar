package com.gmail.goosius.siegewar.events;

import com.gmail.goosius.siegewar.enums.SiegeType;
import com.gmail.goosius.siegewar.objects.SiegeCamp;
import com.palmergames.bukkit.towny.object.Translatable;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SiegePrepareEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final SiegeCamp camp;
    private boolean cancelled;
    private Translatable cancellationMessage = Translatable.of("msg_siege_preparation_cancelled");

    public SiegePrepareEvent(SiegeCamp camp) {
        this.camp = camp;
    }

    public SiegeCamp getCamp() {
        return camp;
    }

    public Player getSiegeStarter() {
        return camp.getPlayer();
    }

    public SiegeType getSiegeType() {
        return camp.getSiegeType();
    }

    public NamespacedKey getSiegeTypeId() {
        return camp.getSiegeTypeId();
    }

    public NamespacedKey getCustomSiegeTypeId() {
        return camp.getCustomSiegeTypeId();
    }

    public void setCustomSiegeTypeId(NamespacedKey id) {
        camp.setCustomSiegeTypeId(id);
    }

    public Integer getBattleSessionsRequired() {
        return camp.getBattleSessionsRequired();
    }

    public void setBattleSessionsRequired(Integer sessions) {
        camp.setBattleSessionsRequired(sessions);
    }

    public boolean isSkipSiegeCamp() {
        return camp.isSkipSiegeCamp();
    }

    public void setSkipSiegeCamp(boolean skip) {
        camp.setSkipSiegeCamp(skip);
    }

    public boolean isSkipSiegeValidation() {
        return camp.isSkipSiegeValidation();
    }

    public void setSkipSiegeValidation(boolean skip) {
        camp.setSkipSiegeValidation(skip);
    }

    public Translatable getCancellationMessage() {
        return cancellationMessage;
    }

    public void setCancellationMessage(Translatable message) {
        cancellationMessage = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
