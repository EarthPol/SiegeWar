package com.gmail.goosius.siegewar;

import com.gmail.goosius.siegewar.api.SiegeTypeDefinition;
import com.gmail.goosius.siegewar.objects.Siege;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class SiegeTypeRegistry implements Listener {
    private record Registration(Plugin owner, SiegeTypeDefinition definition) {}

    private static final Map<NamespacedKey, Registration> types = new ConcurrentHashMap<>();

    public static void register(Plugin owner, NamespacedKey id, SiegeTypeDefinition definition) {
        Objects.requireNonNull(owner, "owner");
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(definition, "definition");
        if (!id.getNamespace().equals(new NamespacedKey(owner, "type").getNamespace())
                || id.getNamespace().equals("siegewar"))
            throw new IllegalArgumentException("Custom type ID must belong to the addon namespace");
        for (Siege siege : SiegeController.getSieges())
            if (id.equals(siege.getCustomSiegeTypeId()) && siege.getSiegeType() != definition.getBaseType())
                throw new IllegalArgumentException("Cannot change the base type of a persisted siege");
        if (types.putIfAbsent(id, new Registration(owner, definition)) != null)
            throw new IllegalArgumentException("Siege type is already registered: " + id);
    }

    public static boolean unregister(Plugin owner, NamespacedKey id) {
        Registration registration = types.get(id);
        return registration != null && registration.owner() == owner && types.remove(id, registration);
    }

    public static SiegeTypeDefinition getDefinition(NamespacedKey id) {
        Registration registration = id == null ? null : types.get(id);
        return registration == null ? null : registration.definition();
    }

    public static boolean isAvailable(NamespacedKey id) {
        Registration registration = id == null ? null : types.get(id);
        return registration != null && registration.owner().isEnabled();
    }

    public static Map<NamespacedKey, SiegeTypeDefinition> getTypes() {
        Map<NamespacedKey, SiegeTypeDefinition> definitions = new HashMap<>();
        types.forEach((id, registration) -> definitions.put(id, registration.definition()));
        return Map.copyOf(definitions);
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        types.entrySet().removeIf(entry -> entry.getValue().owner() == event.getPlugin()
                || event.getPlugin() == SiegeWar.getSiegeWar());
    }
}
