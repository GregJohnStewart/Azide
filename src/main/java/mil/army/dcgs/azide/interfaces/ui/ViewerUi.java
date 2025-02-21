package mil.army.dcgs.azide.interfaces.ui;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.entities.model.ApplicationInfo;
import mil.army.dcgs.azide.service.ApplicationInfoRepository;

@Slf4j
@RequestScoped
@Path("/app/viewer")
@Authenticated
public class ViewerUi extends UiInterface {

    @Getter
    @Location("pages/appviewer")
    Template pageTemplate;

    @Inject
    ApplicationInfoRepository applicationInfoRepository;

    @QueryParam("appRef")
    Optional<String> appRef;
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getViewer() {
        boolean defaultScreen = false;
        ApplicationInfo appInfo;
        
        if(this.appRef.isPresent()){
            appInfo = this.applicationInfoRepository.getAppFromRef(appRef);
        } else {
            appInfo = this.applicationInfoRepository.getSplashAppOrDefault();
            defaultScreen = true;
        }
                                      ;
        log.debug("Loading viewer with app info {}", appInfo);
        
        return this.getDefaultAuthPageSetup()
            .data("app", appInfo)
            .data("defaultScreen", defaultScreen)
            ;
    }
}
