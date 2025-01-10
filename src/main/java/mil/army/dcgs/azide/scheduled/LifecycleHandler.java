package mil.army.dcgs.azide.scheduled;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.service.ClassificationRepository;
import mil.army.dcgs.azide.service.PriorityMessageRepository;
import mil.army.dcgs.azide.service.ApplicationInfoRepository;

@Singleton
@Slf4j
public class LifecycleHandler {

    @Inject
    ClassificationRepository classificationRepository;

    @Inject
    PriorityMessageRepository priorityMessageRepository;

    @Inject
    ApplicationInfoRepository applicationInfoRepository;

    @Transactional
    void onStart(
            @Observes
            StartupEvent ev
    ) {
        this.classificationRepository.populate();
//        this.priorityMessageRepository.populate();
        this.applicationInfoRepository.populate();
    }
}
