package io.github.anjoismysign.alternativesaving.director.manager;

import io.github.anjoismysign.alternativesaving.AlternativeSaving;
import io.github.anjoismysign.alternativesaving.configuration.SavingConfiguration;
import io.github.anjoismysign.alternativesaving.director.SavingManager;
import io.github.anjoismysign.alternativesaving.director.SavingManagerDirector;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConfigurationManager extends SavingManager {
    private static SavingConfiguration configuration;

    public ConfigurationManager(SavingManagerDirector managerDirector) {
        super(managerDirector);
        reload();
    }

    @Override
    public void reload() {
        AlternativeSaving plugin = getPlugin();
        plugin.saveResource("config.yml", false);
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        Constructor constructor = new Constructor(SavingConfiguration.class, new LoaderOptions());
        Yaml yaml = new Yaml(constructor);
        try (FileInputStream inputStream = new FileInputStream(configFile)) {
            configuration = yaml.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @NotNull
    public static SavingConfiguration getConfiguration(){
        return configuration;
    }
}