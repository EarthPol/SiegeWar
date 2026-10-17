package com.gmail.goosius.siegewar.events;

import com.gmail.goosius.siegewar.enums.SiegeType;
import com.gmail.goosius.siegewar.objects.SiegeCamp;
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

public class PreSiegeWarStartEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final SiegeType siegeType;
    private final Town townOfSiegeStarter;
    private final Player siegeStarter;
    private final Nation nation;
    private final Block flag;
    private final TownBlock townBlock;
    private final Town targetTown;
    private NamespacedKey customSiegeTypeId;
    private boolean isCancelled;
    private Translatable cancellationMessage = Translatable.of("msg_siege_start_cancelled");

    public PreSiegeWarStartEvent(SiegeType siegeType,
                                 Town targetTown,
                                 Nation nation,
                                 Town townOfSiegeStarter,
                                 Player siegeStarter,
                                 Block flag,
                                 TownBlock townBlock){
        this.siegeType = siegeType;
        this.targetTown = targetTown;
        this.townOfSiegeStarter = townOfSiegeStarter;
        this.siegeStarter = siegeStarter;
        this.nation = nation;
        this.flag = flag;
        this.townBlock = townBlock;
    }

    public PreSiegeWarStartEvent(SiegeCamp camp) {
        this(camp.getSiegeType(), camp.getTargetTown(), (Nation) camp.getAttacker(),
                camp.getTownOfSiegeStarter(), camp.getPlayer(), camp.getBannerBlock(), camp.getTownBlock());
        customSiegeTypeId = camp.getCustomSiegeTypeId();
    }

    public NamespacedKey getSiegeTypeId() {
        return customSiegeTypeId == null ? new NamespacedKey("siegewar", siegeType.toLowerCase()) : customSiegeTypeId;
    }

    public NamespacedKey getCustomSiegeTypeId() {
        return customSiegeTypeId;
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
        return townOfSiegeStarter;
    }

    public Nation getNation() {
        return nation;
    }

    public Block getFlag() {
        return flag;
    }

    public Player getSiegeStarter() {
        return siegeStarter;
    }

    public TownBlock getTownBlock() {
        return townBlock;
    }

    public Town getTargetTown() {
        return targetTown;
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
        return siegeType;
    }
}
