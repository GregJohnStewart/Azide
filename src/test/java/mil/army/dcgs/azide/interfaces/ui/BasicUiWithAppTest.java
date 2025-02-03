package mil.army.dcgs.azide.interfaces.ui;

import com.microsoft.playwright.Page;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import mil.army.dcgs.azide.testResources.profiles.OneAppFilledProfile;
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
@TestProfile(OneAppFilledProfile.class)
public class BasicUiWithAppTest extends WebUiTest {
	
	@Test
	public void testInitialScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getContext().newPage();
		
		NavUtils.navigateToUrl(page, this.getIndex().toString());
	}
	
	@Test
	public void testLoggedInScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		page.locator(AllPages.NAV_LOGO);
		page.locator(AppViewerPage.USER_MENU_BUTTON);
	}
	
	@Test
	public void testLogOut() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		page.locator(AppViewerPage.USER_MENU_BUTTON).click();
		
		page.locator(AppViewerPage.USER_LOGOUT_BUTTON).click();
		
		MainAssertions.assertDoneProcessing(page);
		
		assertEquals(
			this.getIndex().toString(),
			page.url().split("\\?")[0] //stripping possible state GET params after logging out
		);
	}
	
	@Test
	public void testAppDropdown() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		assertFalse(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		
		page.locator(AppViewerPage.APPS_BUTTON).click();
		
		assertTrue(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
	}
}
