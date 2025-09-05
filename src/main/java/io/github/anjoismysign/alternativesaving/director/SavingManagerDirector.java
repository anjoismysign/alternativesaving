package io.github.anjoismysign.alternativesaving.director;

import io.github.anjoismysign.alternativesaving.director.manager.AlternativeSavingManager;
import io.github.anjoismysign.bloblib.entities.GenericManagerDirector;
import io.github.anjoismysign.alternativesaving.AlternativeSaving;
import io.github.anjoismysign.alternativesaving.director.manager.ConfigurationManager;
import org.jetbrains.annotations.NotNull;

public class SavingManagerDirector extends GenericManagerDirector<AlternativeSaving> {
    public SavingManagerDirector(AlternativeSaving plugin) {
        super(plugin);
        registerBlobInventory("AlternativeSavingWelcomeInventory");
        registerBlobInventory("AlternativeSavingSwitchProfile");
        addManager("ConfigurationManager",
                new ConfigurationManager(this));
        addManager("AlternativeSavingManager",
                new AlternativeSavingManager(this));
    }

    /**
     * From top to bottom, follow the order.
     */
    @Override
    public void reload() {
        getConfigurationManager().reload();
    }

    @Override
    public void unload() {
        getAlternativeSavingManager().unload();
    }

    @NotNull
    public final ConfigurationManager getConfigurationManager() {
        return getManager("ConfigurationManager", ConfigurationManager.class);
    }

    @NotNull
    public final AlternativeSavingManager getAlternativeSavingManager(){
        return getManager("AlternativeSavingManager", AlternativeSavingManager.class);
    }
}