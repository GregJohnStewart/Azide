package mil.army.dcgs.azide.errorhandling;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.qute.Template;
import java.util.HashMap;
import java.util.Map;

@Provider
public class AuthenticationExceptionMapper implements ExceptionMapper<AuthenticationFailedException> {

    // Inject the Qute template (located at src/main/resources/templates/error.html)
    @Inject
    Template errorfragment;

    @Override
    public Response toResponse(AuthenticationFailedException exception) {
        // Prepare data for the error template
        Map<String, Object> data = new HashMap<>();
        data.put("errorTitle", "Authentication Error");
        data.put("errorMessage", exception.getMessage());

        // Render the template and return a 401 Unauthorized response
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(errorfragment.data(data))
                .build();
    }
}
