package io.github.anjoismysign.alternativesaving.blobeconomy;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface BlobEconomy {

    @NotNull Map<String, Double> wallet(@NotNull Player player);

    @NotNull Map<String, Double> bank(@NotNull Player player);

    void applyWallet(@NotNull Player player,
                     @NotNull Map<String,Double> map);

    void applyBank(@NotNull Player player,
                   @NotNull Map<String,Double> map);
}
