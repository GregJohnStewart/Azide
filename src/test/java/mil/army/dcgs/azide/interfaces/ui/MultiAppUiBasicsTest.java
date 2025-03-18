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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(AppFillingProfile.TwoAzApps.class)
public class MultiAppUiBasicsTest extends WebUiTest {
	
	@Test
	public void testAppList() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
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
//		Locator otherAppButton = appsBarLocator.locator(".appSelectCard").nth(1);
		
		//TODO:: verify app list
		
	}
	
	
	@Test
	public void testAppOpen() throws InterruptedException {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		assertFalse(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		page.locator(AppViewerPage.APPS_BUTTON).click();
		assertTrue(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		assertTrue(page.locator(AppViewerPage.APPS_FILTER_INPUT).isVisible());
		
		List<Locator> appsDisplayed = page.locator(AppViewerPage.APPS_BAR).locator("div.appSelectCard").all();
		
		assertEquals(2, appsDisplayed.size());
		
		Locator otherApp = appsDisplayed.getLast();
		
		otherApp.locator(".appOpenNewWindowButton").click();
		
		//TODO:: get other window, test opened and displaying correct app
	}
	
}
