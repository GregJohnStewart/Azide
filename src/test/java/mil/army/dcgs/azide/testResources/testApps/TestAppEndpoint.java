package mil.army.dcgs.azide.testResources.testApps;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.MessageFormat;
import java.util.Optional;

@RequestScoped
@Path("/test/app")
@PermitAll
public class TestAppEndpoint {
	private static final String DEFAULT_APP_REF = "defaultTestApp";
	
	@QueryParam("testAppRef")
	Optional<String> testAppRef;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response get() {
		return Response.ok(
			MessageFormat.format(
				"""
					<html>
					<head>
					<title>Test App {0}</title>
					</head>
					<body>
					
					<h1>Test App {0}</h1>
					
					</body>
					</html>
					""",
				this.testAppRef.orElse("defaultTestApp")
			),
			MediaType.TEXT_HTML
		).build();
	}
	
}
