package io.github.anjoismysign.alternativesaving.command;

import io.github.anjoismysign.alternativesaving.AlternativeSaving;
import io.github.anjoismysign.alternativesaving.director.manager.AlternativeSavingManager;
import io.github.anjoismysign.alternativesaving.entity.SerialPlayer;
import io.github.anjoismysign.bloblib.api.BlobLibMessageAPI;
import io.github.anjoismysign.skeramidcommands.command.Command;
import io.github.anjoismysign.skeramidcommands.command.CommandBuilder;
import io.github.anjoismysign.skeramidcommands.commandtarget.BukkitCommandTarget;
import io.github.anjoismysign.skeramidcommands.server.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public enum AlternativeSavingCommand {
    INSTANCE;

    public void load(){
        Command alternativesaving = CommandBuilder.of("alternativesaving").build();
        switchProfile(alternativesaving);
    }

    public void switchProfile(Command alternativesaving){
        Command profileCommand = alternativesaving.child("profile");
        profileCommand.setParameters(BukkitCommandTarget.ONLINE_PLAYERS());
        profileCommand.onExecute((permissionMessenger, args) -> {
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
