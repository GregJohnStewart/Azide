package mil.army.dcgs.azide.interfaces.ui;

import com.microsoft.playwright.Page;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import mil.army.dcgs.azide.testResources.resources.MessagesAppTestResource;
import mil.army.dcgs.azide.testResources.testClasses.WebUiTest;
import mil.army.dcgs.azide.testResources.testUser.TestUser;
import mil.army.dcgs.azide.testResources.ui.assertions.MainAssertions;
import mil.army.dcgs.azide.testResources.ui.pages.AllPages;
import mil.army.dcgs.azide.testResources.ui.pages.AppViewerPage;
import mil.army.dcgs.azide.testResources.ui.utilities.NavUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@Disabled
@QuarkusTest
@QuarkusTestResource(value = MessagesAppTestResource.class, restrictToAnnotatedClass = true)
public class BasicUiWithMessagesAppTest extends WebUiTest {
	
	@Test
	public void testInitialScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getContext().newPage();
		
		NavUtils.navigateToUrl(page, this.getIndex().toString());
		
		assertTrue(
			page.frameLocator("#appframe")
				.locator("#message-table")
				.isVisible()
		);
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
