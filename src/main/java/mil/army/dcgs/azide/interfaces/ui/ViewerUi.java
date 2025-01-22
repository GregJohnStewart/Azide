package mil.army.dcgs.azide.interfaces.ui;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;

import java.util.Optional;
import java.util.UUID;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.service.ApplicationInfoRepository;

@Slf4j
@RequestScoped
@Path("/app/viewer")
public class ViewerUi extends UiInterface {

    @Getter
    @Location("pages/appviewer")
    Template pageTemplate;

    @Getter
    @Location("apps/no-app")
    Template noAppTemplate;

    @Inject
    ApplicationInfoRepository applicationInfoRepository;

    @QueryParam("appId")
    Optional<String> appId;
	@Inject
	Application application;
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getViewer() {
        return this.getDefaultAuthPageSetup()
            .data("applicationInfoRepository", applicationInfoRepository)
            .data("applicationInfo", applicationInfoRepository.findAll().list())
            .data(
                "appLocation",
                applicationInfoRepository.getAppLocationFromId(appId)
            );
    }

    @GET
    @Path("/no-app")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance noAppPane() {
        return this.getDefaultAuthPageSetup(this.getNoAppTemplate())
            .data("applicationInfoRepository", this.applicationInfoRepository);
    }
    

    @GET
    @Path("/pane/{id}")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getApp(@PathParam("id") String selectedApp) {
        
        return this.getDefaultAuthPageSetup()
            .data("applicationInfo", applicationInfoRepository.findAll().list())
            .data("selectedApp", applicationInfoRepository.find("id", UUID.fromString(selectedApp)).firstResult());

    }
}
