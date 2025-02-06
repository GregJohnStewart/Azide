package mil.army.dcgs.azide.interfaces.ui;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import mil.army.dcgs.azide.testResources.profiles.AppFillingProfile;
import mil.army.dcgs.azide.testResources.testClasses.WebUiTest;
import mil.army.dcgs.azide.testResources.testUser.TestUser;
import mil.army.dcgs.azide.testResources.ui.pages.AllPages;
import mil.army.dcgs.azide.testResources.ui.pages.AppViewerPage;
import mil.army.dcgs.azide.testResources.ui.utilities.NavUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(AppFillingProfile.OneIwcApp.class)
public class IwcOneAppTest extends WebUiTest {
	
	@Test
	public void testLoggedInScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		page.locator(AllPages.NAV_LOGO);
		page.locator(AppViewerPage.USER_MENU_BUTTON);
		
		Locator appTitle = page.frameLocator("#appframe").locator("#appTitle");
		
		assertTrue(appTitle.isVisible());
	}
	
}
