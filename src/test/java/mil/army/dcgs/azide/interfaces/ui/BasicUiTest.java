package mil.army.dcgs.azide.interfaces.ui;

import com.microsoft.playwright.Page;
import io.quarkus.test.junit.QuarkusTest;
import mil.army.dcgs.azide.testResources.testClasses.WebUiTest;
import mil.army.dcgs.azide.testResources.testUser.TestUser;
import mil.army.dcgs.azide.testResources.ui.utilities.NavUtils;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class BasicUiTest extends WebUiTest {
	
	
	@Test
	public void testInitialScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getContext().newPage();
		
		NavUtils.navigateToUrl(page, this.getIndex().toString());
	}
}
