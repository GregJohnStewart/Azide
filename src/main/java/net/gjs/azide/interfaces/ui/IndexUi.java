package net.gjs.azide.interfaces.ui;

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
import net.gjs.azide.service.ProvidedSiteRepository;

import static java.util.Objects.requireNonNull;
import net.gjs.azide.service.ClassificationRepository;
import net.gjs.azide.service.ProvidedMessageRepository;

@RequestScoped
@Path("/")
public class IndexUi extends UiInterface {

    @Getter
    @Location("pages/index")
    Template pageTemplate;

    @Inject
    ProvidedSiteRepository providedSiteRepository;
    
    @Inject
    ProvidedMessageRepository providedMessageRepository;
    
    @Inject
    ClassificationRepository classificationRepository;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance get() {
        return this.getDefaultPageSetup()
                .data("providedSites", providedSiteRepository.findAll().list()).data("providedMessages", providedMessageRepository.findAll().list()).data("classificationBanner", classificationRepository.findAll().list().getFirst());
    }

}
