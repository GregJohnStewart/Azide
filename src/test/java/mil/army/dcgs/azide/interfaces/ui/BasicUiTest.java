package mil.army.dcgs.azide.interfaces.ui;

import com.microsoft.playwright.Page;
import io.quarkus.test.junit.QuarkusTest;
import mil.army.dcgs.azide.testResources.testClasses.WebUiTest;
import mil.army.dcgs.azide.testResources.testUser.TestUser;
import mil.army.dcgs.azide.testResources.ui.assertions.MainAssertions;
import mil.army.dcgs.azide.testResources.ui.pages.AllPages;
import mil.army.dcgs.azide.testResources.ui.pages.AppViewerPage;
import mil.army.dcgs.azide.testResources.ui.pages.AupPage;
import mil.army.dcgs.azide.testResources.ui.pages.IndexPage;
import mil.army.dcgs.azide.testResources.ui.pages.KeycloakUi;
import mil.army.dcgs.azide.testResources.ui.utilities.NavUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class BasicUiTest extends WebUiTest {
	
	
	@Test
	public void testInitialScreen() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getContext().newPage();
		
		NavUtils.navigateToUrl(page, this.getIndex().toString());
	}
	
	@Test
	public void testAUPScreenAccept() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getContext().newPage();
		
		NavUtils.navigateToUrl(page, this.getIndex().toString());
		
		page.locator(IndexPage.LOGIN_LINK).click();
		
		MainAssertions.assertDoneProcessing(page);
		
		KeycloakUi.loginUser(user, page);
		
		page.locator(AupPage.AUP_ACCEPT_BUTTON).click();
		
		MainAssertions.assertDoneProcessing(page);
		
		assertTrue(page.url().endsWith("/app/viewer"));
	}
	
	@Test
	public void testAUPScreenDecline() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getContext().newPage();
		
		NavUtils.navigateToUrl(page, this.getIndex().toString());
		
		page.locator(IndexPage.LOGIN_LINK).click();
		
		MainAssertions.assertDoneProcessing(page);
		
		KeycloakUi.loginUser(user, page);
		
		page.locator(AupPage.AUP_DECLINE_BUTTON).click();
		
		MainAssertions.assertDoneProcessing(page);
		
		assertEquals(
			this.getIndex().toString(),
			page.url().split("\\?")[0] //stripping possible state GET params after logging out
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
}
