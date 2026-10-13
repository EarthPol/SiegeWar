package com.gmail.goosius.siegewar.events;

import com.gmail.goosius.siegewar.enums.SiegeSide;
import com.gmail.goosius.siegewar.objects.Siege;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public class SiegeCompletedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Siege siege;
    private final UUID siegeUUID;
    private final NamespacedKey siegeTypeId;
    private final SiegeSide siegeWinner;
    private final NamespacedKey victoryId;
    private final String victoryName;
    private final UUID townUUID;
    private final Map<String, String> customData;

    public SiegeCompletedEvent(Siege siege) {
        super(!Bukkit.getServer().isPrimaryThread());
        this.siege = siege;
        this.siegeUUID = siege.getUUID();
        this.siegeTypeId = siege.getSiegeTypeId();
        this.siegeWinner = siege.getSiegeWinner();
        this.victoryId = siege.getVictoryId();
        this.victoryName = siege.getTranslatedStatusName().defaultLocale();
        this.townUUID = siege.getTown().getUUID();
        this.customData = Map.copyOf(siege.getCustomData());
    }

    public Siege getSiege() {
        return siege;
    }

    public UUID getSiegeUUID() {
        return siegeUUID;
    }

    public NamespacedKey getSiegeTypeId() {
        return siegeTypeId;
    }

    public SiegeSide getSiegeWinner() {
        return siegeWinner;
    }

    public NamespacedKey getVictoryId() {
        return victoryId;
    }

    public String getVictoryName() {
        return victoryName;
    }

    public UUID getTownUUID() {
        return townUUID;
    }

    public Map<String, String> getCustomData() {
        return customData;
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
