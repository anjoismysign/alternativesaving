package io.github.anjoismysign.alternativesaving.command;

import io.github.anjoismysign.alternativesaving.AlternativeSaving;
import io.github.anjoismysign.alternativesaving.configuration.SavingConfiguration;
import io.github.anjoismysign.alternativesaving.director.manager.AlternativeSavingManager;
import io.github.anjoismysign.alternativesaving.director.manager.ConfigurationManager;
import io.github.anjoismysign.alternativesaving.entity.SerialPlayer;
import io.github.anjoismysign.alternativesaving.entity.SerialProfile;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandBuilder;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public enum AlternativeSavingCommand {
    INSTANCE;

    public void load(){
        Command alternativeSaving = CommandBuilder.of("alternativesaving").build();
        Command profileCommand = alternativeSaving.child("profile");
        switchProfile(profileCommand);
        addProfile(profileCommand);
    }

    public void addProfile(Command profileCommand){
        Command command = profileCommand.child("add");
        command.setParameters(BukkitCommandTarget.ONLINE_PLAYERS());
        command.onExecute((permissionMessenger, args) -> {
            CommandSender sender = BukkitAdapter.getInstance().of(permissionMessenger);
            Player target = BukkitCommandTarget.ONLINE_PLAYERS().parse(args[0]);
            if (target == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", sender)
                        .toCommandSender(sender);
                return;
            }
            @Nullable SerialPlayer serialPlayer = AlternativeSavingManager.getSerialPlayer(target);
            if (serialPlayer == null){
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Inside-Plugin-Cache", target)
                        .toCommandSender(sender);
                return;
            }
            SavingConfiguration configuration = ConfigurationManager.getConfiguration();
            var profiles = serialPlayer.getProfiles();
            var serialProfile = new SerialProfile(UUID.randomUUID().toString(),configuration.getRandomProfileName(profiles.stream().map(SerialProfile::getProfileName).toList()), "", false);
            profiles.add(serialProfile);
            BlobLibMessageAPI.getInstance().getMessage("AlternativeSaving.Added-Profile", target)
                    .modder()
                    .replace("%profileName%", serialProfile.getProfileName())
                    .replace("%player%", target.getName())
                    .get()
                    .toCommandSender(sender);
        });
    }

    public void switchProfile(Command profileCommand){
        Command command = profileCommand.child("switch");
        command.setParameters(BukkitCommandTarget.ONLINE_PLAYERS());
        command.onExecute((permissionMessenger, args) -> {
            CommandSender sender = BukkitAdapter.getInstance().of(permissionMessenger);
            Player target = BukkitCommandTarget.ONLINE_PLAYERS().parse(args[0]);
            if (target == null) {
                BlobLibMessageAPI.getInstance()
                        .getMessage("Player.Not-Found", sender)
                        .toCommandSender(sender);
                return;
            }
            @Nullable SerialPlayer serialPlayer = AlternativeSavingManager.getSerialPlayer(target);
            if (serialPlayer == null){
                BlobLibMessageAPI.getInstance()
                    .getMessage("Player.Not-Inside-Plugin-Cache", target)
                    .handle(target);
                return;
            }
            Bukkit.getScheduler().runTask(AlternativeSaving.getInstance(), () -> {
                if (!target.isConnected()) {
                    return;
                }
                serialPlayer.openProfileSwitch();
            });
        });
    }
}
