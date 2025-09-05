package io.github.anjoismysign.alternativesaving.director.manager;

import io.github.anjoismysign.alternativesaving.AlternativeSaving;
import io.github.anjoismysign.alternativesaving.configuration.SavingConfiguration;
import io.github.anjoismysign.alternativesaving.configuration.WelcomeInventoryConfiguration;
import io.github.anjoismysign.alternativesaving.configuration.WelcomePlayersConfiguration;
import io.github.anjoismysign.alternativesaving.director.SavingManager;
import io.github.anjoismysign.alternativesaving.director.SavingManagerDirector;
import io.github.anjoismysign.alternativesaving.entity.SerialPlayer;
import io.github.anjoismysign.bloblib.SoulAPI;
import io.github.anjoismysign.bloblib.api.BlobLibInventoryAPI;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.bloblib.entities.inventory.InventoryBuilderCarrier;
import io.github.anjoismysign.bloblib.entities.inventory.MetaBlobPlayerInventoryBuilder;
import io.github.anjoismysign.bloblib.entities.inventory.MetaInventoryButton;
import io.github.anjoismysign.bloblib.entities.translatable.TranslatableItem;
import io.github.anjoismysign.bloblib.managers.cruder.BukkitCruder;
import io.github.anjoismysign.bloblib.managers.cruder.BukkitCruderBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AlternativeSavingManager extends SavingManager {
    private final String reference = "AlternativeSavingWelcomeInventory";
    private final BukkitCruder<SerialPlayer> serialPlayerCruder;

    private static AlternativeSavingManager INSTANCE;

    @Nullable
    public static SerialPlayer getSerialPlayer(@NotNull Player player){
        Objects.requireNonNull(player, "'player' cannot be null");
        return INSTANCE.serialPlayerCruder.get(player);
    }

    public AlternativeSavingManager(SavingManagerDirector director) {
        super(director);
        INSTANCE = this;
        reload();
        AlternativeSaving plugin = AlternativeSaving.getInstance();
        serialPlayerCruder = new BukkitCruderBuilder<SerialPlayer>()
                .crudableClass(SerialPlayer.class)
                .plugin(plugin)
                .onJoin(serialPlayer -> {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        SavingConfiguration savingConfiguration = ConfigurationManager.getConfiguration();
                        @Nullable Player joined = serialPlayer.getPlayer();
                        if (joined == null){
                            return;
                        }
                        if (serialPlayer.hasPlayedBefore()){
                            serialPlayer.loadProfile(joined, serialPlayer.getSelectedProfile());
                            boolean translateOnJoin = savingConfiguration.isTranslateOnJoin();
                            if (translateOnJoin){
                                String locale = joined.getLocale();
                                for (ItemStack stack : joined.getInventory().getContents()) {
                                    TranslatableItem.localize(stack, locale);
                                }
                                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                    if (!joined.isOnline()){
                                        return;
                                    }
                                    joined.updateInventory();
                                }, 5);
                            }
                            return;
                        }
                        WelcomePlayersConfiguration welcomePlayers = savingConfiguration.getWelcomePlayers();
                        if (welcomePlayers.isEnabled()) {
                            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                                BlobLibMessageAPI.getInstance()
                                        .getMessage(welcomePlayers.getMessage(), onlinePlayer)
                                        .modder()
                                        .replace("%player%", joined.getName())
                                        .get()
                                        .handle(onlinePlayer);
                            });
                        }
                        WelcomeInventoryConfiguration inventoryConfiguration = welcomePlayers.getInventory();
                        if (inventoryConfiguration.isEnabled()) {
                            InventoryBuilderCarrier<MetaInventoryButton> carrier = BlobLibInventoryAPI
                                    .getInstance().getMetaInventoryBuilderCarrier(reference, joined.getLocale());
                            Objects.requireNonNull(carrier, "'" + reference + "' cannot be null");
                            MetaBlobPlayerInventoryBuilder.fromInventoryBuilderCarrier
                                    (carrier, joined.getUniqueId());
                            if (inventoryConfiguration.isSoul()){
                                SoulAPI.getInstance().set(joined);
                            }
                        }
                    });
                })
                .onAutoSave(serialPlayer -> {
                    @Nullable Player player = serialPlayer.getPlayer();
                    if (player == null){
                        return;
                    }
                    serialPlayer.saveCurrentProfile(player, serialPlayer.hasPlayedBefore());
                })
                .onQuit(serialPlayer -> {
                    @Nullable Player player = serialPlayer.getPlayer();
                    if (player == null){
                        return;
                    }
                    serialPlayer.saveCurrentProfile(player, true);
                })
                .build();
    }

    @Override
    public void reload() {
        SavingConfiguration configuration = ConfigurationManager.getConfiguration();
        if (configuration.getDefaultSlots() < 1){
            throw new RuntimeException("config.yml 'defaultSlots' cannot be less than one");
        }
    }

    @Override
    public void unload() {
        serialPlayerCruder.saveAll();
    }
}
