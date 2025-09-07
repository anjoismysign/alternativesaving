package io.github.anjoismysign.alternativesaving.entity;

import org.jetbrains.annotations.NotNull;

public class SerialProfile {
    private final String profileName;
    protected String json;
    protected boolean hasPlayedBefore;

    public SerialProfile(@NotNull String profileName,
                         @NotNull String json,
                         boolean hasPlayedBefore){
        this.profileName = profileName;
        this.json = json;
        this.hasPlayedBefore = hasPlayedBefore;
    }

    public String getProfileName() {
        return profileName;
    }

    public @NotNull String getJson() {
        return json;
    }

    public boolean hasPlayedBefore() {
        return hasPlayedBefore;
    }
}
