package io.github.anjoismysign.alternativesaving.entity;

import org.jetbrains.annotations.NotNull;

public class SerialProfile {
    private final String profileName;
    protected String json;

    public SerialProfile(@NotNull String profileName,
                         @NotNull String json){
        this.profileName = profileName;
        this.json = json;
    }

    public String getProfileName() {
        return profileName;
    }

    public @NotNull String getJson() {
        return json;
    }
}
