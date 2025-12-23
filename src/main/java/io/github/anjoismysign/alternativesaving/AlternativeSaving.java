package io.github.anjoismysign.alternativesaving;

import io.github.anjoismysign.alternativesaving.command.AlternativeSavingCommand;
import io.github.anjoismysign.alternativesaving.configuration.SavingConfiguration;
import io.github.anjoismysign.alternativesaving.director.manager.ConfigurationManager;
import io.github.anjoismysign.bloblib.managers.BlobPlugin;
import io.github.anjoismysign.bloblib.managers.IManagerDirector;
import io.github.anjoismysign.alternativesaving.director.SavingManagerDirector;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class AlternativeSaving extends BlobPlugin {
    private IManagerDirector proxy;

    private static AlternativeSaving INSTANCE;

    public static AlternativeSaving getInstance(){
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        SavingManagerDirector director = new SavingManagerDirector(this);
        proxy = director.proxy();
        AlternativeSavingCommand.INSTANCE.load();
    }

    public void info(@NotNull String message){
        SavingConfiguration savingConfiguration = ConfigurationManager.getConfiguration();
        if (!savingConfiguration.isTinyDebug()){
            return;
        }
        Logger logger = this.getLogger();
        logger.info(message);
    }

    public IManagerDirector getManagerDirector() {
        return proxy;
    }
}
