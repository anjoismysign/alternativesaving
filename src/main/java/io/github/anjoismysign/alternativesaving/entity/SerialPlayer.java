package io.github.anjoismysign.alternativesaving.entity;

import io.github.anjoismysign.alternativesaving.AlternativeSaving;
import io.github.anjoismysign.alternativesaving.configuration.SavingConfiguration;
import io.github.anjoismysign.alternativesaving.director.manager.ConfigurationManager;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.psa.crud.Crudable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SerialPlayer implements Crudable {
    private final @NotNull String identification;
    private final @NotNull List<SerialProfile> profiles;
    private int selectedProfile = 0;

    public SerialPlayer(@NotNull String identification){
        SavingConfiguration configuration = ConfigurationManager.getConfiguration();
        this.identification = identification;
        this.profiles = new ArrayList<>();
        int defaultSlots = configuration.getDefaultSlots();
        for (int index = 0; index < defaultSlots; index++) {
            this.profiles.add(new SerialProfile(UUID.randomUUID().toString(),configuration.getRandomProfileName(profiles.stream().map(SerialProfile::getProfileName).toList()),
                    "", false));
        }
    }

    @Override
    public @NotNull String getIdentification() {
        return identification;
    }

    public @NotNull List<SerialProfile> getProfiles() {
        return profiles;
    }

    public int getSelectedProfile() {
        return selectedProfile;
    }

    public void setSelectedProfile(int selectedProfile) {
        this.selectedProfile = selectedProfile;
    }

    public int getProfilesSize(){
        return profiles.size();
    }

    public void saveCurrentProfile(@NotNull Player player,
                                   boolean hasPlayedBefore){
        int size = profiles.size();
        if (size <= selectedProfile){
            throw new RuntimeException("Selected profile is '"+selectedProfile+"' but there are only "+size+" profiles");
        }
        SerialProfile selected = profiles.get(selectedProfile);
        selected.hasPlayedBefore = hasPlayedBefore;
        selected.json = PlayerProfile.fromPlayer(player,hasPlayedBefore).toJson();
    }

    public void loadProfile(@NotNull Player player,
                            int selectedProfile){
        AlternativeSaving plugin = AlternativeSaving.getInstance();
        if (this.selectedProfile != selectedProfile) {
            saveCurrentProfile(player, true);
        }
        int size = profiles.size();
        if (size <= selectedProfile){
            throw new RuntimeException("Selected profile is '"+selectedProfile+"' but there are only "+size+" profiles");
        }
        SerialProfile profile = profiles.get(selectedProfile);
        String json = profile.json;
        if (json == null || json.isEmpty()){
            plugin.info("empty json");
            profile.json = PlayerProfile.fromPlayer(player, false).toJson();
        } else {
            PlayerProfile.fromJson(json).toPlayer(player);
        }
        this.selectedProfile = selectedProfile;
    }

    @Nullable
    public Player getPlayer(){
        UUID uuid = UUID.fromString(getIdentification());
        return Bukkit.getPlayer(uuid);
    }

    public void openProfileSwitch(){
        @Nullable Player target = getPlayer();
        if (target == null){
            return;
        }
        BlobLibInventoryAPI.getInstance()
                .customSelector(
                        "AlternativeSavingSwitchProfile",
                        target,
                        "Profiles",
                        "Profile",
                        ()->profiles,
                        profile -> {
                            target.closeInventory();
                            int index = profiles.indexOf(profile);
                            if (index == -1){
                                throw new RuntimeException("Profile does not belong to SerialPlayer");
                            }
                            if (index == selectedProfile){
                                return;
                            }
                            loadProfile(target, index);
                        },
                        profile -> TranslatableItem.by("AlternativeSaving.Switch-Profile-Element")
                                .localize(target)
                                .modder()
                                .replace("%profile%", profile.getProfileName())
                                .get()
                                .get(),
                        null,
                        null,
                        null);
    }
}
