package mil.army.dcgs.azide.testResources.testClasses;

import io.quarkus.test.common.http.TestHTTPResource;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.testUser.TestUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.net.URL;

import static io.restassured.RestAssured.given;

@Slf4j
@Execution(ExecutionMode.SAME_THREAD)
public class RunningServerTest extends WebServerTest {
	
	@Getter
	@TestHTTPResource("/")
	URL index;
	
	@Getter
	private final TestUserService testUserService = TestUserService.getInstance();
	
	@BeforeEach
	public void beforeEach(TestInfo testInfo) {
		log.info("Before test " + testInfo.getTestMethod().get().getName());
	}
	
	@AfterEach
	public void afterEach(
		TestInfo testInfo
	) {
		log.info("Running after method for test {}", testInfo.getDisplayName());
		
		//TODO:: clear db?
		
		log.info("Completed after step.");
	}
	
}
