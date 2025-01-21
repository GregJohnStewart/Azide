package mil.army.dcgs.azide.interfaces.ui;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.Getter;

@RequestScoped
@Path("/app/aup")
public class AUPUi extends UiInterface {
    
    @Getter
    @Location("pages/aup")
    Template pageTemplate;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public TemplateInstance get() {
        return this.getDefaultAuthPageSetup();
    }

    

}
