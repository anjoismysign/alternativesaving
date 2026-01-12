package io.github.anjoismysign.alternativesaving.entity;

import org.jetbrains.annotations.NotNull;

public class SerialProfile {
    private final String profileName;
    protected String json;
    protected boolean hasPlayedBefore;
    private final String identification;

    public SerialProfile(@NotNull String identification,
                         @NotNull String profileName,
                         @NotNull String json,
                         boolean hasPlayedBefore){
        this.identification = identification;
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

    public String getIdentification() {
        return identification;
    }
}
