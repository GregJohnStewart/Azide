package net.gjs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.gjs.azide.config.ClassificationBannerConfig;
import net.gjs.azide.entities.model.ClassificationBanner;


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

        log.info("Populating provided classification.");

        ClassificationBanner output;
        this.persist(
                        ClassificationBanner.builder()
                                .classification(classificationBannerConfig.classification())
                                .color(classificationBannerConfig.color())
                                .build()
                );
        this.initted = true;
    }

    

}