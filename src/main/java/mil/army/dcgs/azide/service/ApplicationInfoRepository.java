package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;

import mil.army.dcgs.azide.config.ApplicationInfoConfig;

import mil.army.dcgs.azide.entities.model.ApplicationInfo;


@Slf4j
@Named("ApplicationInfoRepository")
@ApplicationScoped
public class ApplicationInfoRepository implements PanacheRepository<ApplicationInfo> {

    @Inject
    ApplicationInfoConfig applicationInfoConfig;

    private boolean initted = false;

    public void populate() {
        if(this.initted){
            return;
        }

        log.info("Populating applications.");

        for (ApplicationInfoConfig.Application application : this.applicationInfoConfig.applications()) {
            // Optional<ApplicationInfo> fromDb = this.find("name", application.name()).firstResultOptional();

            // if (fromDb.isEmpty()) {
                log.debug("Adding new application: {}", application.name());
                this.persist(
                    ApplicationInfo.builder()
                        .name(application.name())
                        .location(application.location())
                        .image(application.image())
                        .build()
                );
//            }
        }
        this.initted = true;    
    }

}