package io.github.anjoismysign.alternativesaving;

import io.github.anjoismysign.alternativesaving.command.AlternativeSavingCommand;
import io.github.anjoismysign.bloblib.managers.BlobPlugin;
import io.github.anjoismysign.bloblib.managers.IManagerDirector;
import io.github.anjoismysign.alternativesaving.director.SavingManagerDirector;

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

    public IManagerDirector getManagerDirector() {
        return proxy;
    }
}
