package net.gjs.azide.scheduled;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.gjs.azide.service.ProvidedSiteRepository;

@Singleton
@Slf4j
public class LifecycleHandler {

    @Inject
    ProvidedSiteRepository providedSiteRepository;

    @Transactional
    void onStart(
            @Observes
            StartupEvent ev
    ) {
        this.providedSiteRepository.populate();
    }
}
