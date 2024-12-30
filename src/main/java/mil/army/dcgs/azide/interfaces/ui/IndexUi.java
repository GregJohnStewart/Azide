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
import mil.army.dcgs.azide.service.ProvidedMessageRepository;
import mil.army.dcgs.azide.service.PriorityMessageRepository;

@RequestScoped
@Path("/")
public class IndexUi extends UiInterface {

    @Getter
    @Location("pages/index")
    Template pageTemplate;
    
    @Inject
    ProvidedMessageRepository providedMessageRepository;
    
    @Inject
    ClassificationRepository classificationRepository;
    
    @Inject
    PriorityMessageRepository priorityMessageRepository;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance get() {
        return this.getDefaultPageSetup()
                .data("providedMessages", providedMessageRepository.findAll().list())
                .data("priorityMessages", priorityMessageRepository.findAll().list())
                .data("classificationBanner", classificationRepository.findAll().list().getFirst());

    }

}
