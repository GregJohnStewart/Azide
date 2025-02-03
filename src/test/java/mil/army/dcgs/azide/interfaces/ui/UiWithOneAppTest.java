package mil.army.dcgs.azide.interfaces.ui;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import mil.army.dcgs.azide.testResources.profiles.AppFillingProfile;
import mil.army.dcgs.azide.testResources.testClasses.WebUiTest;
import mil.army.dcgs.azide.testResources.testUser.TestUser;
import mil.army.dcgs.azide.testResources.ui.assertions.MainAssertions;
import mil.army.dcgs.azide.testResources.ui.pages.AllPages;
import mil.army.dcgs.azide.testResources.ui.pages.AppViewerPage;
import mil.army.dcgs.azide.testResources.ui.utilities.NavUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(AppFillingProfile.OneApp.class)
public class UiWithOneAppTest extends WebUiTest {
	
	@Test
	public void testInitialScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getContext().newPage();
		
		NavUtils.navigateToUrl(page, this.getIndex().toString());
		
		Locator appTitle = page.frameLocator("#appframe").locator("#appTitle");
		
		assertTrue(appTitle.isVisible());
	}
	
	@Test
	public void testLoggedInScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		page.locator(AllPages.NAV_LOGO);
		page.locator(AppViewerPage.USER_MENU_BUTTON);
		
		Locator appTitle = page.frameLocator("#appframe").locator("#appTitle");
		
		assertTrue(appTitle.isVisible());
	}
	
	@Test
	public void testAppDropdown() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		assertFalse(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		
		page.locator(AppViewerPage.APPS_BUTTON).click();
		
		assertTrue(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		assertFalse(page.locator(AppViewerPage.APPS_FILTER_INPUT).isDisabled());
		assertFalse(page.locator(AppViewerPage.NO_APPS_AVAILABLE_ALERT).isVisible());
		
		
	}
}
