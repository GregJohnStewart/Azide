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

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@TestProfile(AppFillingProfile.OneAzApp.class)
public class AzWindowBasicsTest extends WebUiTest {
	
	@Test
	public void testWindowInit() {
		TestUser user = this.getTestUserService().getUser();
		Page page = this.newPage();
		PageLog pageLog = this.getLog(page);
		
		Pattern windowInitBannerLog = Pattern.compile("======== Initializing new Azide window:\nazideWindow");
		
		pageLog.registerPattern(windowInitBannerLog);
		
		this.loginPage(page, user);
		
		pageLog.assertPatternHits(windowInitBannerLog, 1);
		
		//TODO:: get console message from app about being initted, getting parent id. assert got the right one.
	}
	
}
