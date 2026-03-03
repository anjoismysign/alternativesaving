package io.github.anjoismysign.alternativesaving.entity;

import org.bukkit.GameMode;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class PlayerProfileBean {
        private double health;
        private double maxHealth;
        private int foodLevel;
        private float saturation;
        private float exhaustion;
        private int level;
        private float exp;
        private int totalExperience;
        private float fallDistance;
        private int fireTicks;
        private int remainingAir;
        private int maximumAir;
        private boolean allowFlight;
        private boolean flying;
        private @NotNull GameMode gameMode;
        private int heldItemSlot;
        private boolean playedBefore;
        private @NotNull Map<String, Double> wallet;
        private @NotNull Map<String, Double> bank;

        public PlayerProfileBean() {
        }

        public PlayerProfile toPlayerProfile(){
            return new PlayerProfile(
                    health,
                    maxHealth,
                    foodLevel,
                    saturation,
                    exhaustion,
                    level,
                    exp,
                    totalExperience,
                    fallDistance,
                    fireTicks,
                    remainingAir,
                    maximumAir,
                    allowFlight,
                    flying,
                    gameMode,
                    "",
                    "",
                    "",
                    heldItemSlot,
                    playedBefore,
                    wallet,
                    bank);
        }

        public double getHealth() {
            return health;
        }

        public void setHealth(double health) {
            this.health = health;
        }

        public double getMaxHealth() {
            return maxHealth;
        }

        public void setMaxHealth(double maxHealth) {
            this.maxHealth = maxHealth;
        }

        public int getFoodLevel() {
            return foodLevel;
        }

        public void setFoodLevel(int foodLevel) {
            this.foodLevel = foodLevel;
        }

        public float getSaturation() {
            return saturation;
        }

        public void setSaturation(float saturation) {
            this.saturation = saturation;
        }

        public float getExhaustion() {
            return exhaustion;
        }

        public void setExhaustion(float exhaustion) {
            this.exhaustion = exhaustion;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public float getExp() {
            return exp;
        }

        public void setExp(float exp) {
            this.exp = exp;
        }

        public int getTotalExperience() {
            return totalExperience;
        }

        public void setTotalExperience(int totalExperience) {
            this.totalExperience = totalExperience;
        }

        public float getFallDistance() {
            return fallDistance;
        }

        public void setFallDistance(float fallDistance) {
            this.fallDistance = fallDistance;
        }

        public int getFireTicks() {
            return fireTicks;
        }

        public void setFireTicks(int fireTicks) {
            this.fireTicks = fireTicks;
        }

        public int getRemainingAir() {
            return remainingAir;
        }

        public void setRemainingAir(int remainingAir) {
            this.remainingAir = remainingAir;
        }

        public int getMaximumAir() {
            return maximumAir;
        }

        public void setMaximumAir(int maximumAir) {
            this.maximumAir = maximumAir;
        }

        public boolean isAllowFlight() {
            return allowFlight;
        }

        public void setAllowFlight(boolean allowFlight) {
            this.allowFlight = allowFlight;
        }

        public boolean isFlying() {
            return flying;
        }

        public void setFlying(boolean flying) {
            this.flying = flying;
        }

        public @NotNull GameMode getGameMode() {
            return gameMode;
        }

        public void setGameMode(@NotNull GameMode gameMode) {
            this.gameMode = gameMode;
        }

        public int getHeldItemSlot() {
            return heldItemSlot;
        }

        public void setHeldItemSlot(int heldItemSlot) {
            this.heldItemSlot = heldItemSlot;
        }

    public boolean isPlayedBefore() {
        return playedBefore;
    }

    public void setPlayedBefore(boolean playedBefore) {
        this.playedBefore = playedBefore;
    }

        public @NotNull Map<String, Double> getWallet() {
            return wallet;
        }

        public void setWallet(@NotNull Map<String, Double> wallet) {
            this.wallet = wallet;
        }

        public @NotNull Map<String, Double> getBank() {
            return bank;
        }

        public void setBank(@NotNull Map<String, Double> bank) {
            this.bank = bank;
        }
    }