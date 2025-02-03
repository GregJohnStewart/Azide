package mil.army.dcgs.azide.interfaces.ui;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
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
        //TODO:: update to show splash app if not have app from ref
        return this.getDefaultAuthPageSetup()
            .data("app", this.applicationInfoRepository.getAppFromRef(appRef));
    }
}
