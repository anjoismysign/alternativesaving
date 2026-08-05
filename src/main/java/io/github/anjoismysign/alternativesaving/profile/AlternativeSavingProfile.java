package io.github.anjoismysign.alternativesaving.profile;

import io.github.anjoismysign.alternativesaving.director.manager.AlternativeSavingManager;
import io.github.anjoismysign.alternativesaving.entity.SerialPlayer;
import io.github.anjoismysign.alternativesaving.entity.SerialProfile;
import net.milkbowl.vault.profile.Profile;
import net.milkbowl.vault.profile.wrappers.ProfileWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AlternativeSavingProfile implements Profile {

    public static void load(){
        new ProfileWrapper(new AlternativeSavingProfile()).registerProviders();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "AlternativeSaving";
    }

    @Nullable
    private SerialPlayer serialPlayer(@NotNull OfflinePlayer player) {
        Player online = Bukkit.getPlayer(player.getUniqueId());
        if (online != null) {
            return AlternativeSavingManager.getSerialPlayer(online);
        }
        return null;
    }

    @Nullable
    private SerialProfile serialProfile(@NotNull SerialPlayer serialPlayer,
                                        int index) {
        if (index < 0 || index >= serialPlayer.getProfilesSize()) {
            return null;
        }
        return serialPlayer.getProfiles().get(index);
    }

    @Override
    public int getProfileCount(@NotNull OfflinePlayer player) {
        SerialPlayer serialPlayer = serialPlayer(player);
        return serialPlayer == null ? 0 : serialPlayer.getProfilesSize();
    }

    @Override
    public @Nullable String getProfileIdentification(@NotNull OfflinePlayer player,
                                                     int index) {
        SerialPlayer serialPlayer = serialPlayer(player);
        SerialProfile serialProfile = serialPlayer == null ? null : serialProfile(serialPlayer, index);
        return serialProfile == null ? null : serialPlayer.getIdentification() + ":" + serialProfile.getIdentification();
    }

    @Override
    public @Nullable String getProfileName(@NotNull OfflinePlayer player,
                                           int index) {
        SerialPlayer serialPlayer = serialPlayer(player);
        SerialProfile serialProfile = serialPlayer == null ? null : serialProfile(serialPlayer, index);
        return serialProfile == null ? null : serialProfile.getProfileName();
    }

    @Override
    public boolean hasProfilePlayedBefore(@NotNull OfflinePlayer player,
                                          int index) {
        SerialPlayer serialPlayer = serialPlayer(player);
        SerialProfile serialProfile = serialPlayer == null ? null : serialProfile(serialPlayer, index);
        return serialProfile != null && serialProfile.hasPlayedBefore();
    }

    @Override
    public int getCurrentProfileIndex(@NotNull OfflinePlayer player) {
        SerialPlayer serialPlayer = serialPlayer(player);
        return serialPlayer == null ? -1 : serialPlayer.getSelectedProfile();
    }

    @Override
    public boolean switchProfile(@NotNull OfflinePlayer player,
                                 int index) {
        SerialPlayer serialPlayer = serialPlayer(player);
        if (serialPlayer == null) {
            return false;
        }
        if (serialProfile(serialPlayer, index) == null) {
            return false;
        }
        Player online = Bukkit.getPlayer(player.getUniqueId());
        if (online == null) {
            return false;
        }
        serialPlayer.loadProfile(online, index, true);
        return true;
    }
}
