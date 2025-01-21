package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

import mil.army.dcgs.azide.config.ApplicationInfoConfig;

import mil.army.dcgs.azide.entities.model.ApplicationInfo;


@Slf4j
@Named("ApplicationInfoRepository")
@ApplicationScoped
public class ApplicationInfoRepository implements PanacheRepository<ApplicationInfo> {

    @Inject
    ApplicationInfoConfig applicationInfoConfig;

    private boolean initted = false;

    public String getApplicationId(String appName) {
        List<ApplicationInfo> apps = findAll().list();

        for(ApplicationInfo app : apps) {
            if(app.getName().equalsIgnoreCase(appName)) {
                return app.getId().toString();
            }
        }

        return null;
    }

    public String getApplicationLocation(String appName) {
        List<ApplicationInfo> apps = findAll().list();

        for(ApplicationInfo app : apps) {
            if(app.getName().equalsIgnoreCase(appName)) {
                return app.getLocation();
            }
        }

        return null;
    }
    
//    public String getDescription(String appdescription) {
//        List<ApplicationInfo> apps = findAll().list();
//
//        for(ApplicationInfo app : apps) {
//            if(app.getDescription().isPresent()) {
//                if(app.getDescription().get().equalsIgnoreCase(appdescription)) {
//                    return app.getDescription().get();
//                }
//            }
//        }
//
//        return null;
//    }

    public void populate() {
        if(this.initted){
            return;
        }

        log.info("Populating applications.");

        for (ApplicationInfoConfig.Application application : this.applicationInfoConfig.applications()) {
            // Optional<ApplicationInfo> fromDb = this.find("name", application.name()).firstResultOptional();
            // if (fromDb.isEmpty()) {
                log.debug("Adding new application: {}", application.name());
                if(application.description().isPresent()) {
                    this.persist(

                        ApplicationInfo.builder()
                            .name(application.name())
                            .description(application.description().get())
                            .location(application.location())
                            .image(application.image())
                            .showInAppBar(application.showInAppBar())
                            .build()                
                    );
                } else {
                    this.persist(

                        ApplicationInfo.builder()
                            .name(application.name())
                            .location(application.location())
                            .image(application.image())
                            .showInAppBar(application.showInAppBar())
                            .build()                
                    );
                }
//            }
        }
        this.initted = true;    
    }

}