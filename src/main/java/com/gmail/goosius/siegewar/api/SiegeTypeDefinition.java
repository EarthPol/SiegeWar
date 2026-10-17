package com.gmail.goosius.siegewar.api;

import com.gmail.goosius.siegewar.enums.SiegeType;
import com.palmergames.bukkit.towny.object.Translatable;

import java.util.Objects;

public final class SiegeTypeDefinition {
    private final SiegeType baseType;
    private final Translatable translatedName;
    private final boolean allowInvading;
    private final boolean allowPlundering;

    public SiegeTypeDefinition(SiegeType baseType, Translatable translatedName) {
        this(baseType, translatedName, true, true);
    }

    public SiegeTypeDefinition(SiegeType baseType, Translatable translatedName, boolean allowInvading, boolean allowPlundering) {
        this.baseType = Objects.requireNonNull(baseType, "baseType");
        this.translatedName = Objects.requireNonNull(translatedName, "translatedName");
        this.allowInvading = allowInvading;
        this.allowPlundering = allowPlundering;
    }

    public SiegeType getBaseType() {
        return baseType;
    }

    public Translatable getTranslatedName() {
        return translatedName;
    }

    public boolean allowsInvading() {
        return allowInvading;
    }

    public boolean allowsPlundering() {
        return allowPlundering;
    }
}
