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

@RequestScoped
@Path("/")
public class IndexUi extends UiInterface {

    @Getter
    @Location("pages/index")
    Template pageTemplate;
    
    @Inject
    ClassificationRepository classificationRepository;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance get() {
        return this.getDefaultPageSetup().data("classificationBanner", classificationRepository.findAll().list().getFirst());
    }

}
