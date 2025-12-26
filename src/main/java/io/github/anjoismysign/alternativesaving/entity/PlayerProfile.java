package io.github.anjoismysign.alternativesaving.entity;

import com.google.gson.Gson;
import io.github.anjoismysign.alternativesaving.blobeconomy.BlobEconomy;
import io.github.anjoismysign.alternativesaving.blobeconomy.Found;
import io.github.anjoismysign.alternativesaving.blobeconomy.NotFound;
import io.github.anjoismysign.bloblib.utilities.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record PlayerProfile(
        double getHealth,
        double getMaxHealth,
        int getFoodLevel,
        float getSaturation,
        float getExhaustion,
        int getLevel,
        float getExp,
        int getTotalExperience,
        float getFallDistance,
        int getFireTicks,
        int getRemainingAir,
        int getMaximumAir,
        boolean getAllowFlight,
        boolean getFlying,
        @NotNull GameMode getGameMode,
        @NotNull String getLocation,
        @NotNull String getInventory,
        @NotNull String getArmor,
        int getHeldItemSlot,
        boolean hasPlayedBefore,
        @NotNull Map<String, Double> wallet,
        @NotNull Map<String, Double> bank
) {

    public static PlayerProfile fromPlayer(@NotNull Player player,
                                           boolean hasPlayedBefore) {
        Location location = player.getLocation();
        String serialLocation = location.getWorld().getName()
                + "," + location.getX()
                + "," + location.getY()
                + "," + location.getZ()
                + "," + location.getYaw()
                + "," + location.getPitch();
        BlobEconomy economy;
        if (Bukkit.getPluginManager().isPluginEnabled("BlobEconomy")) {
            economy = new Found();
        } else {
            economy = new NotFound();
        }

        return new PlayerProfile(
                player.getHealth(),
                player.getAttribute(Attribute.MAX_HEALTH).getValue(),
                player.getFoodLevel(),
                player.getSaturation(),
                player.getExhaustion(),
                player.getLevel(),
                player.getExp(),
                player.getTotalExperience(),
                player.getFallDistance(),
                player.getFireTicks(),
                player.getRemainingAir(),
                player.getMaximumAir(),
                player.getAllowFlight(),
                player.isFlying(),
                player.getGameMode(),
                serialLocation,
                ItemStackUtil.itemStackArrayToBase64(player.getInventory().getContents()),
                ItemStackUtil.itemStackArrayToBase64(player.getInventory().getArmorContents()),
                player.getInventory().getHeldItemSlot(),
                hasPlayedBefore,
                economy.wallet(player),
                economy.bank(player)
        );
    }

    public static PlayerProfile fromJson(@NotNull String json) {
        return new Gson().fromJson(json, PlayerProfile.class);
    }

    public void toPlayer(@NotNull Player player) {
        BlobEconomy economy;
        if (Bukkit.getPluginManager().isPluginEnabled("BlobEconomy")) {
            economy = new Found();
        } else {
            economy = new NotFound();
        }

        player.setHealth(this.getHealth);
        AttributeInstance maxHealthInstance = player.getAttribute(Attribute.MAX_HEALTH);
        if (maxHealthInstance != null) {
            maxHealthInstance.setBaseValue(this.getMaxHealth);
        }
        player.setFoodLevel(this.getFoodLevel);
        player.setSaturation(this.getSaturation);
        player.setExhaustion(this.getExhaustion);
        player.setLevel(this.getLevel);
        player.setExp(this.getExp);
        player.setTotalExperience(this.getTotalExperience);
        player.setFallDistance(this.getFallDistance);
        player.setFireTicks(this.getFireTicks);
        player.setRemainingAir(this.getRemainingAir);
        player.setMaximumAir(this.getMaximumAir);
        player.setAllowFlight(this.getAllowFlight);
        player.setFlying(this.getFlying);
        player.setGameMode(this.getGameMode);

        String[] parts = this.getLocation.split(",");
        if (parts.length == 6) {
            World world = Bukkit.getWorld(parts[0]);
            try {
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);
                float yaw = Float.parseFloat(parts[4]);
                float pitch = Float.parseFloat(parts[5]);
                if (world != null) {
                    player.teleport(new Location(world, x, y, z, yaw, pitch));
                }
            } catch (NumberFormatException ignored) {
            }
        }

        ItemStack[] contents = ItemStackUtil.itemStackArrayFromBase64(this.getInventory);
        ItemStack[] armor = ItemStackUtil.itemStackArrayFromBase64(this.getArmor);
        player.getInventory().setContents(contents);
        player.getInventory().setArmorContents(armor);
        player.getInventory().setHeldItemSlot(this.getHeldItemSlot);

        economy.applyWallet(player, wallet);
        economy.applyBank(player, bank);

    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}

