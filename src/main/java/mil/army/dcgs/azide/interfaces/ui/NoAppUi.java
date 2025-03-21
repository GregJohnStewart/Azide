package mil.army.dcgs.azide.interfaces.ui;

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

@RequestScoped
@Path("/app/viewer/no-app")
@PermitAll
public class NoAppUi extends UiInterface {

    @Getter
    @Location("apps/no-app")
    Template pageTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return this.getDefaultPageSetup();
    }

}
