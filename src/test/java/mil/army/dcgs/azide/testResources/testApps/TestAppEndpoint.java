package mil.army.dcgs.azide.testResources.testApps;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.DefaultValue;
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
	
	@DefaultValue("false")
	@QueryParam("iwc")
	boolean iwc;
	
	@DefaultValue("false")
	@QueryParam("azApp")
	boolean azApp;
	
	private String getAppRef() {
		return this.testAppRef.orElse(DEFAULT_APP_REF);
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response get() {
		
		String js = "";
		
		if (this.iwc) {
			js = MessageFormat.format(
				"""
					<script src="/res/js/iwc.js"></script>
					<script>
					let iwc = new Iwc(new IwcConfig('{' appName: "{0}" }));
					
					</script>
					""",
				this.getAppRef()
			);
		} else if (this.azApp) {
			
			js = MessageFormat.format(
				"""
					<script src="/res/js/iwc.js"></script>
					<script src="/res/js/AzideApp.js"></script>
					<script>
					let azApp = new AzideApp('{' appName: "{0}" '}');
					</script>
					""",
				this.getAppRef()
			);
		}
		
		
		return Response.ok(
			MessageFormat.format(
				"""
					<html>
					<head>
					<title>Test App {0}</title>
					<style>
						body '{'
							background-color: white;
						}
					</style>
					</head>
					<body>
					
					<h1 id="appTitle">Test App <span id="appRef">{0}</span></h1>
					
					
					{1}
					</body>
					</html>
					""",
				this.getAppRef(),
				js
			),
			MediaType.TEXT_HTML
		).build();
	}
	
}
