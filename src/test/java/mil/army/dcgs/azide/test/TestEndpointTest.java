package mil.army.dcgs.azide.test;

import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.testClasses.RunningServerTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

@Slf4j
@QuarkusTest
public class TestEndpointTest extends RunningServerTest {
	
	@Test
	public void testEndpoint() {
		String endpoint = this.getIndex().toString() + "test/app";
		log.info("Hitting test endpoint: {}", endpoint);
		when().get(endpoint)
			.then()
			.statusCode(200);
	}
}
