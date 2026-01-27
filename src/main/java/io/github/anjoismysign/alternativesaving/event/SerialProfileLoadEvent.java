
package io.github.anjoismysign.alternativesaving.event;

import io.github.anjoismysign.alternativesaving.entity.SerialPlayer;
import io.github.anjoismysign.alternativesaving.entity.SerialProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called on main thread
 */
public class SerialProfileLoadEvent extends Event {
    private final SerialPlayer serialPlayer;
    private final SerialProfile serialProfile;
    private final Player player;

    public SerialProfileLoadEvent(@NotNull SerialPlayer serialPlayer,
                                  @NotNull SerialProfile serialProfile,
                                  @NotNull Player player){
        super(false);
        this.serialPlayer = serialPlayer;
        this.serialProfile = serialProfile;
        this.player = player;
    }

    @NotNull
    public SerialPlayer getSerialPlayer() {
        return serialPlayer;
    }

    @NotNull
    public SerialProfile getSerialProfile() {
        return serialProfile;
    }

    @NotNull
    public Player getPlayer(){
        return player;
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
