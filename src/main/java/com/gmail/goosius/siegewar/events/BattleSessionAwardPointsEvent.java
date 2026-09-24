package com.gmail.goosius.siegewar.events;

import com.gmail.goosius.siegewar.objects.Siege;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fired when SiegeWar awards (or effectively applies) battle points during a battle session.
 *
 * This event is informational/observability only. Listeners should NOT modify SiegeWar state.
 */
public class BattleSessionAwardPointsEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public enum Reason {
        DEATH,
        KILLED_BY_PLAYER,
        OTHER
    }

    private final @NotNull Siege siege;
    private final int pointsAwarded; // positive number of points awarded to the opposing side
    private final @NotNull Reason reason;

    private final boolean victimWasAttacker; // true if the player that died was an attacker
    private final @NotNull Player victim;
    private final @Nullable Player killer;

    public BattleSessionAwardPointsEvent(@NotNull Siege siege,
                                         int pointsAwarded,
                                         @NotNull Reason reason,
                                         boolean victimWasAttacker,
                                         @NotNull Player victim,
                                         @Nullable Player killer) {
        // use default async state (false). This event is fired from main thread in SiegeWarScoringUtil.
        this.siege = siege;
        this.pointsAwarded = pointsAwarded;
        this.reason = reason;
        this.victimWasAttacker = victimWasAttacker;
        this.victim = victim;
        this.killer = killer;
    }

    public @NotNull Siege getSiege() {
        return siege;
    }

    /** Always positive. */
    public int getPointsAwarded() {
        return pointsAwarded;
    }

    public @NotNull Reason getReason() {
        return reason;
    }

    public boolean isVictimWasAttacker() {
        return victimWasAttacker;
    }

    public @NotNull Player getVictim() {
        return victim;
    }

    public @Nullable Player getKiller() {
        return killer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
}
