package mil.army.dcgs.azide.interfaces.ui;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.service.ApplicationInfoRepository;
import mil.army.dcgs.azide.service.ClassificationRepository;
import mil.army.dcgs.azide.service.PriorityMessageRepository;

@Slf4j
@RequestScoped
@Path("/app/viewer")
public class ViewerUi extends UiInterface {

    @Getter
    @Location("pages/appviewer")
    Template pageTemplate;

    @Getter
    @Location("apps/prioritymsg-editor")
    Template messageEditorTemplate;
    
    @Inject
    ClassificationRepository classificationRepository;
    
    @Inject
    PriorityMessageRepository priorityMessageRepository;

    @Inject
    ApplicationInfoRepository applicationInfoRepository;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getViewer() {
        return this.getDefaultAuthPageSetup()
            .data("priorityMessages", priorityMessageRepository.findAll().list())
            .data("applicationInfo", applicationInfoRepository.findAll().list())
            .data("classificationBanner", classificationRepository.findAll().list().getFirst())
            .data("selectedApp", applicationInfoRepository.findAll().list().getFirst());
        }

    @GET
    @Path("/message-editor")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance msgEditorPane() {
        return this.getDefaultAuthPageSetup(this.getMessageEditorTemplate())
            .data("priorityMessages", priorityMessageRepository.findAll().list())
            .data("selectedApp", applicationInfoRepository.findAll().list().getFirst());
    }
    
    @GET
    @Path("/pane/{id}")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getApp(@PathParam("id") String selectedApp) {
        
        log.info("APPLICATIONID: {}", selectedApp);
        log.info("APPLICATIONLOCATION: {}", applicationInfoRepository.find("id", UUID.fromString(selectedApp)).firstResult().getLocation());
        return this.getDefaultAuthPageSetup()
            .data("applicationInfo", applicationInfoRepository.findAll().list())
            .data("classificationBanner", classificationRepository.findAll().list().getFirst())
            .data("selectedApp", applicationInfoRepository.find("id", UUID.fromString(selectedApp)).firstResult());

    }
}
