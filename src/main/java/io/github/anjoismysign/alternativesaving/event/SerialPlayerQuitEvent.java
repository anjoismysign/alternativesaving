package io.github.anjoismysign.alternativesaving.event;

import io.github.anjoismysign.alternativesaving.entity.SerialPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SerialPlayerQuitEvent extends Event {
    private final SerialPlayer serialPlayer;

    public SerialPlayerQuitEvent(@NotNull SerialPlayer serialPlayer){
        super(false);
        this.serialPlayer = serialPlayer;
    }

    public SerialPlayer getSerialPlayer() {
        return serialPlayer;
    }

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
