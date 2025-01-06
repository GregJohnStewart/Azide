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
import mil.army.dcgs.azide.service.ApplicationInfoRepository;
import mil.army.dcgs.azide.service.ClassificationRepository;
import mil.army.dcgs.azide.service.PriorityMessageRepository;

@RequestScoped
@Path("/app/viewer")
public class ViewerUi extends UiInterface {

    @Getter
    @Location("pages/appviewer")
    Template pageTemplate;

    @Getter
    @Location("apps/prioritymsg-editor")
    Template paneTemplate;

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
            .data("classificationBanner", classificationRepository.findAll().list().getFirst());
        }

    @GET
    @Path("/pane")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getPane() {
        return this.getDefaultAuthPageSetup(this.getPaneTemplate())
            .data("priorityMessages", priorityMessageRepository.findAll().list());
    }
}
