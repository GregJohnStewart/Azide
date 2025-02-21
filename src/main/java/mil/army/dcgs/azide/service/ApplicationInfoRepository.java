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
    private static final ApplicationInfo DEFAULT_APP = ApplicationInfo.builder()
                                                           .name("")
                                                           .location(DEFAULT_URI)
                                                           .build();
    
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
    
    public ApplicationInfo getSplashAppOrDefault() {
        return this.appOrDefault(this.getSplashApp());
    }
    
    public List<ApplicationInfo> getAppBarApps(){
        return this.find("showInAppBar", true).list();
    }
    
    /**
     * This is only required for a workaround for Qute not properly typing the resulting list
     * @return
     */
    public List<ApplicationInfo> getAllApps(){
        return this.findAll().list();
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
                    .image(applicationFromConfig.image().orElse(null))
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
                log.info("Created new application: {}", newAppInfo);
            }
        }
        this.initted = true;
        log.info("Finished populating appInfo.");
    }
    
    public ApplicationInfo appOrDefault(Optional<ApplicationInfo> app) {
        return app.orElse(DEFAULT_APP);
    }
    
    public ApplicationInfo getAppFromId(Optional<String> appId){
        if(appId.isPresent()) {
            return this.appOrDefault(this.find("id", appId).firstResultOptional());
        }
        log.info("No app id given.");
        
        return DEFAULT_APP;
    }
    
    public ApplicationInfo getAppFromRef(Optional<String> appRef){
        if(appRef.isPresent()) {
            return this.appOrDefault(this.find("reference", appRef.get()).firstResultOptional());
        }
        log.info("No app reference given.");
        
        return DEFAULT_APP;
    }
}