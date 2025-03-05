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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(AppFillingProfile.TwoApps.class)
public class IwcMultiAppTest extends WebUiTest {
	
	@Test
	public void testLoggedInScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		page.locator(AllPages.NAV_LOGO);
		page.locator(AppViewerPage.USER_MENU_BUTTON);
		
		{//Verify starting app
			Locator appTitleLocator = page.frameLocator("#appframe").locator("#appTitle");
			assertTrue(appTitleLocator.isVisible());
			Locator appRefLocator = appTitleLocator.locator("#appRef");
			assertTrue(appRefLocator.isVisible());
			String appRef = appRefLocator.textContent().trim();
			assertEquals("test-service-0", appRef);
		}
		
		assertFalse(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		page.locator(AppViewerPage.APPS_BUTTON).click();
		assertTrue(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		assertTrue(page.locator(AppViewerPage.APPS_FILTER_INPUT).isVisible());
		
		Locator appsBarLocator = page.locator(AppViewerPage.APPS_BAR);
		Locator otherAppButton = appsBarLocator.locator(".appSelectCard").nth(1);
		
		
		
		
		
		
	}
	
}
