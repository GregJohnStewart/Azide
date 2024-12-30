package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.config.ClassificationBannerConfig;
import mil.army.dcgs.azide.entities.model.ClassificationBanner;

@Slf4j
@Named("ClassificationRepository")
@ApplicationScoped
public class ClassificationRepository implements PanacheRepository<ClassificationBanner> {

    @Inject
    ClassificationBannerConfig classificationBannerConfig;

    private boolean initted = false;

    public void populate() {
        if(this.initted){
            return;
        }

        log.info("Populating classification banner info.");

        this.persist(
            ClassificationBanner.builder()
                    .classification(classificationBannerConfig.classification())
                    .color(classificationBannerConfig.color())
                    .build()
            );

        this.initted = true;
    }
}