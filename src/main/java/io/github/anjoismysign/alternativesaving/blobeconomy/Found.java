package io.github.anjoismysign.alternativesaving.blobeconomy;

import io.github.anjoismysign.blobeconomy.BlobEconomyAPI;
import io.github.anjoismysign.bloblib.api.BlobLibEconomyAPI;
import io.github.anjoismysign.bloblib.entities.currency.Wallet;
import io.github.anjoismysign.bloblib.vault.multieconomy.ElasticEconomy;
import net.milkbowl.vault.economy.IdentityEconomy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Found implements BlobEconomy{
    private final BlobEconomyAPI economyAPI = BlobEconomyAPI.getInstance();

    public void applyWallet(@NotNull Player player,
                            @Nullable Map<String,Double> map){
        if (map == null){
            return;
        }
        ElasticEconomy elasticEconomy = BlobLibEconomyAPI.getInstance().getElasticEconomy();
        map.forEach((currency, amount)->{
            if (!elasticEconomy.existsImplementation(currency)){
                return;
            }
            IdentityEconomy identityEconomy = elasticEconomy.getImplementation(currency);
            double current = identityEconomy.getBalance(player);
            identityEconomy.withdrawPlayer(player, current);
            elasticEconomy.getImplementation(currency).depositPlayer(player, amount);
        });
    }

    public void applyBank(@NotNull Player player,
                          @Nullable Map<String,Double> map){
        if (map == null){
            return;
        }
        Wallet wallet = economyAPI.getBankWallet(player);
        wallet.clear();
        wallet.putAll(map);
    }

    @Override
    public @NotNull Map<String, Double> wallet(@NotNull Player player) {
        ElasticEconomy elasticEconomy = BlobLibEconomyAPI.getInstance().getElasticEconomy();
        Map<String,Double> map = new HashMap<>();
        economyAPI.getAllCurrencies().forEach(currency -> {
            String key = currency.getKey();
            IdentityEconomy economy = elasticEconomy.getImplementation(key);
            double amount = economy.getBalance(player);
            map.put(key, amount);
        });
        return map;
    }

    @Override
    public @NotNull Map<String, Double> bank(@NotNull Player player) {
        return new HashMap<>(economyAPI.getBankWallet(player));
    }
}
