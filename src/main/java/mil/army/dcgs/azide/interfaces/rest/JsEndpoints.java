package mil.army.dcgs.azide.interfaces.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Path("/res/js/")
public class JsEndpoints extends RestInterface {
	
	private static String readFile(String path) {
		try (
			InputStream is = JsEndpoints.class.getResourceAsStream(path);
		) {
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final String IFC_CONTENT;
	private static final String AZ_APP_CONTENT;
	private static final String COMBO;
	
	static {
		IFC_CONTENT = readFile("/META-INF/resources/res/js/ifc.js");
		AZ_APP_CONTENT = readFile("/META-INF/resources/res/js/AzideApp.js");
		COMBO = IFC_CONTENT + System.lineSeparator() + AZ_APP_CONTENT;
	}
	
	@GET
	@Path("ifc-azApp-bundle.js")
	@Produces("text/javascript")
	public String iwcAzAppBundle() {
		return COMBO;
	}
}
