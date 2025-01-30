package mil.army.dcgs.azide.testResources.ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.testUser.TestUser;

@Slf4j
public class KeycloakUi {
	
	
	public static void loginUser(TestUser user, Page page){
		log.info("Need to log in user.");
		
		Locator locator = page.locator("#password");
		locator.fill(user.getPassword());
		locator = page.locator("#username");
		locator.fill(user.getName());
		
		page.locator("#kc-login").click();
		page.waitForLoadState();
		
		if (!page.locator(AllPages.NAV_LOGO).isVisible()) {
			throw new IllegalStateException("Not logged in.");
		}
		log.info("Logged in user: " + user);
	}
}
