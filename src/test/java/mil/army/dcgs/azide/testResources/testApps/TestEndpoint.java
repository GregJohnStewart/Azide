package mil.army.dcgs.azide.testResources.testApps;

import io.quarkus.qute.TemplateInstance;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("/test/app")
@PermitAll
public class TestEndpoint {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response get() {
		return Response.ok("Worked!").build();
	}

}
