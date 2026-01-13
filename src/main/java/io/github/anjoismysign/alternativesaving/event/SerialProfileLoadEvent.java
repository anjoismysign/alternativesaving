
package io.github.anjoismysign.alternativesaving.event;

import io.github.anjoismysign.alternativesaving.entity.SerialProfile;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SerialProfileLoadEvent extends Event {
    private final Player player;
    private final SerialProfile serialProfile;

    public SerialProfileLoadEvent(@NotNull SerialProfile serialProfile,
                                  @NotNull Player player){
        super(false);
        this.serialProfile = serialProfile;
        this.player = player;
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
