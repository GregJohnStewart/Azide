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
import net.gjs.azide.service.ClassificationRepository;
import net.gjs.azide.service.ProvidedSiteRepository;

@RequestScoped
@Path("/viewer")
public class ViewerUi extends UiInterface {

    @Getter
    @Location("pages/viewerPage")
    Template pageTemplate;

    @Getter
    @Location("components/viewer/viewerPane")
    Template paneTemplate;

    @Inject
    ProvidedSiteRepository providedSiteRepository;
    
    @Inject
    ClassificationRepository classificationRepository;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getViewer() {
        return this.getDefaultPageSetup()
                .data("providedSites", providedSiteRepository.findAll().list()).data("classificationBanner", classificationRepository.findAll().list().getFirst());
    }

    @GET
    @Path("pane")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance getPane() {
        return this.getDefaultPageSetup(this.getPaneTemplate())
                .data("providedSites", providedSiteRepository.findAll().list());
    }



}
