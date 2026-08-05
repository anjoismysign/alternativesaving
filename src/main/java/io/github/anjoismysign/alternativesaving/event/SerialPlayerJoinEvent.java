package io.github.anjoismysign.alternativesaving.event;

import io.github.anjoismysign.alternativesaving.entity.SerialPlayer;
import io.github.anjoismysign.alternativesaving.entity.SerialProfile;
import net.milkbowl.vault.profile.ProfileLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SerialPlayerJoinEvent extends Event {
    private final SerialPlayer serialPlayer;

    public SerialPlayerJoinEvent(@NotNull SerialPlayer serialPlayer){
        super(false);
        this.serialPlayer = serialPlayer;
        Player player = serialPlayer.getPlayer();
        if (player == null) {
            return;
        }
        SerialProfile currentProfile = serialPlayer.getProfiles().get(serialPlayer.getSelectedProfile());
        Bukkit.getPluginManager().callEvent(new ProfileLoadEvent(player,
                serialPlayer.getIdentification() + ":" + currentProfile.getIdentification(),
                currentProfile.getProfileName(), false));
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
