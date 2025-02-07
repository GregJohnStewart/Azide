package mil.army.dcgs.azide.interfaces.ui;

import com.microsoft.playwright.Page;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.service.ApplicationInfoRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@QuarkusTest
public class NoAppUiTest extends WebUiTest {
	
	@Inject
	ApplicationInfoRepository apps;
	
	@Test
	public void testAppDropdown() {
		TestUser user = this.getTestUserService().getUser();
		
		Page page = this.getLoggedInPage(user);
		
		assertFalse(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		
		page.locator(AppViewerPage.APPS_BUTTON).click();
		
		assertTrue(page.locator(AppViewerPage.APPS_SELECT_BAR).isVisible());
		assertTrue(page.locator(AppViewerPage.APPS_FILTER_INPUT).isVisible());
		assertTrue(page.locator(AppViewerPage.APPS_FILTER_INPUT).isDisabled());
		assertTrue(page.locator(AppViewerPage.NO_APPS_AVAILABLE_ALERT).isVisible());
	}
}
