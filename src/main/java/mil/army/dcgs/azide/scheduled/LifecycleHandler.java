package mil.army.dcgs.azide.scheduled;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.service.ClassificationRepository;
import mil.army.dcgs.azide.service.ProvidedMessageRepository;
import mil.army.dcgs.azide.service.ProvidedSiteRepository;

@Singleton
@Slf4j
public class LifecycleHandler {

    @Inject
    ProvidedSiteRepository providedSiteRepository;
    
    @Inject
    ProvidedMessageRepository providedMessageRepository;
    
    @Inject
    ClassificationRepository classificationRepository;

    @Transactional
    void onStart(
            @Observes
            StartupEvent ev
    ) {
        this.providedSiteRepository.populate();
        this.providedMessageRepository.populate();
        this.classificationRepository.populate();
    }
}
