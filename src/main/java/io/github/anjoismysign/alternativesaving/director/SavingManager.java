package io.github.anjoismysign.alternativesaving.director;

import io.github.anjoismysign.alternativesaving.AlternativeSaving;
import io.github.anjoismysign.bloblib.manager.GenericManager;

public class SavingManager extends GenericManager<AlternativeSaving, SavingManagerDirector> {
    public SavingManager(SavingManagerDirector managerDirector) {
        super(managerDirector);
    }
}