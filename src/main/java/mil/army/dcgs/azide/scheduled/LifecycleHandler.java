package mil.army.dcgs.azide.scheduled;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.service.ApplicationInfoRepository;

@Singleton
@Slf4j
public class LifecycleHandler {

    @Inject
    ApplicationInfoRepository applicationInfoRepository;

    @Transactional
    void onStart(
            @Observes
            StartupEvent ev
    ) {
        this.applicationInfoRepository.populate();
    }
}
