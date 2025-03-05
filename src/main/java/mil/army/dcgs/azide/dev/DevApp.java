package mil.army.dcgs.azide.dev;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.MessageFormat;
import java.util.Optional;

@IfBuildProfile(anyOf = {"dev", "test"})
@Path("/dev/app")
@PermitAll
public class DevApp {
	
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
	public Response getAppPage() {
		
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
	
	
	@GET
	@Path("image")
	@Produces(MediaType.TEXT_HTML)
	public Response getAppImage() {
		return Response.ok(
				MessageFormat.format(
					"""
						<svg viewBox="0 0 240 80" xmlns="http://www.w3.org/2000/svg">
						  <style>
						    .small '{'
						      font: italic 13px sans-serif;
						    }
						    .heavy '{'
						      font: bold 30px sans-serif;
						    }
						
						    /* Note that the color of the text is set with the    *
						     * fill property, the color property is for HTML only */
						    .Rrrrr '{'
						      font: italic 40px serif;
						      fill: red;
						    }
						  </style>
						
						  <text x="20" y="35" class="small">This</text>
						  <text x="40" y="35" class="heavy">app</text>
						  <text x="55" y="55" class="small">is</text>
						  <text x="65" y="55" class="Rrrrr">{0}</text>
						</svg>
						
						""",
					this.getAppRef()
				),
				"image/svg+xml"
			)
				   .header(
					   "Content-Disposition",
					   "attachment;filename=appImage.svg"
				   )
				   .build();
	}
}
