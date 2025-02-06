package mil.army.dcgs.azide.interfaces.rest;

import jakarta.decorator.Delegate;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import mil.army.dcgs.azide.service.ApplicationInfoRepository;

@Path(RestInterface.BASE_PATH + "/apps")
public class AppEndpoints extends RestInterface {
	
	@Inject
	ApplicationInfoRepository applicationInfoRepository;

	@DELETE
	@Path("clear")
	//TODO:: put behind role
	public Response clearApps(){
		
		this.applicationInfoRepository.deleteAll();
		return Response.ok().build();
	}
}
