package mil.army.dcgs.azide.interfaces.ui.apps;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;
import mil.army.dcgs.azide.interfaces.ui.UiInterface;

@RequestScoped
@Path("/app/viewer/profile")
@PermitAll
public class ProfileViewerUi extends UiInterface {

    @Getter
    @Location("pages/profile")
    Template pageTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return this.getDefaultAuthPageSetup();
    }
}
