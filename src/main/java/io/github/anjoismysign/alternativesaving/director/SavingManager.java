package io.github.anjoismysign.alternativesaving.director;

import io.github.anjoismysign.bloblib.entities.GenericManager;
import io.github.anjoismysign.alternativesaving.AlternativeSaving;

public class SavingManager extends GenericManager<AlternativeSaving, SavingManagerDirector> {
    public SavingManager(SavingManagerDirector managerDirector) {
        super(managerDirector);
    }
}