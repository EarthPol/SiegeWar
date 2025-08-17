package com.gmail.goosius.siegewar.utils;

import com.gmail.goosius.siegewar.SiegeWar;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.util.Colors;
import com.palmergames.util.StringMgmt;
import org.bukkit.entity.Player;

import static com.palmergames.bukkit.towny.TownyMessaging.sendMessage;

public class SiegeWarMessageUtil {
    /**
     * Send a message to All online residents of a nation and log
     * preceded by the [NationName], translated for the end-user.
     *
     * @param nation Nation to pass the message to, prefix message with.
     * @param message Translatable object to be messaged to the player using their locale.
     */
    public static void sendPrefixedNationMessage(Nation nation, Translatable message) {
        SiegeWar.info(Colors.strip("[Nation Msg] " + StringMgmt.remUnderscore(nation.getName()) + ": " + message.translate()));

        for (Player player : TownyAPI.getInstance().getOnlinePlayers(nation))
            sendMessage(player, Translatable.of("siegewar_plugin_prefix", StringMgmt.remUnderscore(nation.getName())).append(message));
    }

    /**
     * Send a message to All online residents of a town and log
     * preceded by the [Townname], translated for the end-user.
     *
     * @param town Town to pass the message to, and prefix message with.
     * @param message Translatable object to be messaged to the player using their locale.
     */
    public static void sendPrefixedTownMessage(Town town, Translatable message) {
        SiegeWar.info(Colors.strip("[Town Msg] " + StringMgmt.remUnderscore(town.getName()) + ": " + message.translate()));

        for (Player player : TownyAPI.getInstance().getOnlinePlayers(town))
            sendMessage(player, Translatable.of("siegewar_plugin_prefix", StringMgmt.remUnderscore(town.getName())).append(message));
    }
}
