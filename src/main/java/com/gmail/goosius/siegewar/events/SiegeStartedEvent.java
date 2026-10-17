package com.gmail.goosius.siegewar.events;

import com.gmail.goosius.siegewar.objects.Siege;
import com.palmergames.bukkit.towny.object.Translatable;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SiegeStartedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Siege siege;
    private final Translatable message;

    public SiegeStartedEvent(Siege siege, Translatable message) {
        this.siege = siege;
        this.message = message;
    }

    public Siege getSiege() {
        return siege;
    }

    public Translatable getTranslatedMessage() {
        return message;
    }

    public String getMessage() {
        return PlainTextComponentSerializer.plainText().serialize(
                LegacyComponentSerializer.legacySection().deserialize(message.defaultLocale()));
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
