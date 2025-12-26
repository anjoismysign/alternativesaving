package io.github.anjoismysign.alternativesaving.blobeconomy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class NotFound implements BlobEconomy{

    @Override
    public @NotNull Map<String, Double> wallet(@NotNull Player player) {
        return Map.of();
    }

    @Override
    public @NotNull Map<String, Double> bank(@NotNull Player player) {
        return Map.of();
    }

    @Override
    public void applyWallet(@NotNull Player player, @NotNull Map<String, Double> map) {
    }

    @Override
    public void applyBank(@NotNull Player player, @NotNull Map<String, Double> map) {
    }
}
