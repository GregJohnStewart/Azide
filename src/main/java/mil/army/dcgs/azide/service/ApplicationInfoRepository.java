package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import mil.army.dcgs.azide.config.ApplicationInfoConfig;

import mil.army.dcgs.azide.entities.model.ApplicationInfo;


@Slf4j
@Named("ApplicationInfoRepository")
@ApplicationScoped
public class ApplicationInfoRepository implements PanacheRepository<ApplicationInfo> {
    
    private static final URI DEFAULT_URI = URI.create("/app/viewer/no-app");

    @Inject
    ApplicationInfoConfig applicationInfoConfig;

    private boolean initted = false;

    public String getApplicationId(String appName) {
        List<ApplicationInfo> apps = findAll().list();

        for (ApplicationInfo app : apps) {
            if (app.getName().equalsIgnoreCase(appName)) {
                return app.getId().toString();
            }
        }

        return null;
    }

    public URI getApplicationLocation(String appName) {
        List<ApplicationInfo> apps = findAll().list();

        for (ApplicationInfo app : apps) {
            if (app.getName().equalsIgnoreCase(appName)) {
                return app.getLocation();
            }
        }

        return null;
    }

    public Optional<ApplicationInfo> getDefaultApp() {
        return this.find("defaultApp", true).firstResultOptional();
    }

    public Optional<ApplicationInfo> getSplashApp() {
        return this.find("splashApp", true).firstResultOptional();
    }

    public void populate() {
        if (this.initted) {
            return;
        }

        log.info("Populating applications from config.");
        for (ApplicationInfoConfig.Application applicationFromConfig : this.applicationInfoConfig.applications()) {
            log.debug("Application from config: {}", applicationFromConfig.name());

            Optional<ApplicationInfo> existing = this.find("reference", applicationFromConfig.reference()).singleResultOptional();
            ApplicationInfo newAppInfo = ApplicationInfo.builder()
                    .name(applicationFromConfig.name())
                    .reference(applicationFromConfig.reference())
                    .description(applicationFromConfig.description().orElse(null))
                    .location(applicationFromConfig.location())
                    .image(applicationFromConfig.image())
                    .showInAppBar(applicationFromConfig.showInAppBar())
                    .defaultApp(applicationFromConfig.defaultApp())
                    .splashApp(applicationFromConfig.splashApp())
                    .splashEndpoint(applicationFromConfig.splashEndpoint().orElse(null))
                    .build()
                    ;

            if(existing.isPresent()) {
                ApplicationInfo existingApp = existing.get();
                log.info("Application already exists: {}", existingApp.getName());
                //TODO:: update, if necessary
            } else {
                log.info("Creating new application: {}", newAppInfo.getName());
                this.persist(newAppInfo);
                log.debug("Created new application: {}", newAppInfo);
            }
        }
        this.initted = true;
    }
    
    public URI getAppLocation(Optional<ApplicationInfo> app){
        URI output = DEFAULT_URI;
        if(app.isPresent()) {
            output = app.get().getLocation();
        }
        log.debug("URI for app: {}", output);
        return output;
    }
    
    public URI getAppLocationFromId(Optional<String> appId){
        if(appId.isPresent()) {
            return getAppLocation(this.find("id", appId).firstResultOptional());
        }
        log.info("No app id given.");
        return DEFAULT_URI;
    }
}