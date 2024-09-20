package net.gjs.interfaces.ui;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;

import static java.util.Objects.requireNonNull;

@Path("/")
public class IndexUi extends UiInterface {

    @Getter
    @Location("pages/index")
    Template pageTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance get() {
        return this.getDefaultPageSetup();
    }

}
