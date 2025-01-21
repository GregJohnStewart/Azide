package mil.army.dcgs.azide.interfaces.ui;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;

import mil.army.dcgs.azide.service.PriorityMessageRepository;
import mil.army.dcgs.azide.service.ApplicationInfoRepository;

@RequestScoped
@Path("/")
public class IndexUi extends UiInterface {

    @Getter
    @Location("pages/index")
    Template pageTemplate;
    
    @Getter
    @Location("apps/prioritymsg-viewer")
    Template messageViewerTemplate;
    
    @Inject
    PriorityMessageRepository priorityMessageRepository;
    
    @Inject
    ApplicationInfoRepository applicationInfoRepository;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance get() {
        return this.getDefaultPageSetup()
            .data("priorityMessages", priorityMessageRepository.findAll().list());
    }

    @GET
    @Path("/message-viewer")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance msgViewerPane() {
        return this.getDefaultPageSetup(this.getMessageViewerTemplate())
            .data("applicationInfoRepository", applicationInfoRepository)
            .data("priorityMessages", priorityMessageRepository.findAll().list());
    }
}
