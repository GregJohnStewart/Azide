package net.gjs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.gjs.azide.config.ProvidedSitesConfig;
import net.gjs.azide.entities.model.ProvidedSite;

import java.util.Optional;

@Slf4j
@Named("ProvidedSiteRepository")
@ApplicationScoped
public class ProvidedSiteRepository implements PanacheRepository<ProvidedSite> {

    @Inject
    ProvidedSitesConfig providedSitesConfig;

    private boolean initted = false;

    public void populate() {
        if(this.initted){
            return;
        }

        log.info("Populating provided sites.");

        for (ProvidedSitesConfig.Site curSite : this.providedSitesConfig.sites()) {
            Optional<ProvidedSite> fromDb = this.find("uri", curSite.uri()).firstResultOptional();

            if (fromDb.isEmpty()) {
                log.debug("Adding new site: {}", curSite.uri());
                this.persist(
                        ProvidedSite.builder()
                                .title(curSite.title())
                                .description(curSite.description())
                                .uri(curSite.uri())
                                .build()
                );
            }
        }
        this.initted = true;
    }

}