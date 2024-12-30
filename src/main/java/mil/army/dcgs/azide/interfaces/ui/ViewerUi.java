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
import mil.army.dcgs.azide.service.ClassificationRepository;
import mil.army.dcgs.azide.service.PriorityMessageRepository;

@RequestScoped
@Path("/app/viewer")
public class ViewerUi extends UiInterface {

    @Getter
    @Location("pages/viewerPage")
    Template pageTemplate;

    @Getter
    @Location("components/viewer/viewerPane")
    Template paneTemplate;

    @Inject
    ClassificationRepository classificationRepository;
    
    @Inject
    PriorityMessageRepository priorityMessageRepository;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getViewer() {
        return this.getDefaultPageSetup()
            .data("priorityMessages", priorityMessageRepository.findAll().list())
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
