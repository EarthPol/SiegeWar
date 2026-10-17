package com.gmail.goosius.siegewar.events;

import com.gmail.goosius.siegewar.enums.SiegeSide;
import com.gmail.goosius.siegewar.objects.Siege;
import com.palmergames.bukkit.towny.object.Translatable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PreSiegeEndEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Siege siege;
    private SiegeSide siegeWinner;
    private NamespacedKey customVictoryId;
    private Translatable customVictoryName;
    private boolean cancelled;

    public PreSiegeEndEvent(Siege siege, SiegeSide winner, NamespacedKey id, Translatable name) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.siege = Objects.requireNonNull(siege);
        setSiegeWinner(winner);
        setCustomVictory(id, name);
    }

    public Siege getSiege() {
        return siege;
    }

    public NamespacedKey getSiegeTypeId() {
        return siege.getSiegeTypeId();
    }

    public SiegeSide getSiegeWinner() {
        return siegeWinner;
    }

    public void setSiegeWinner(SiegeSide winner) {
        siegeWinner = Objects.requireNonNull(winner);
    }

    public NamespacedKey getCustomVictoryId() {
        return customVictoryId;
    }

    public Translatable getCustomVictoryName() {
        return customVictoryName;
    }

    public void setCustomVictory(NamespacedKey id, Translatable name) {
        if ((id == null) != (name == null))
            throw new IllegalArgumentException("Victory id and name must be set together.");
        customVictoryId = id;
        customVictoryName = name;
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
