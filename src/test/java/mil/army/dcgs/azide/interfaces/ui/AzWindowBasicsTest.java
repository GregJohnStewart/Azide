package mil.army.dcgs.azide.interfaces.ui;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.profiles.AppFillingProfile;
import mil.army.dcgs.azide.testResources.testClasses.WebUiTest;
import mil.army.dcgs.azide.testResources.testUser.TestUser;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@QuarkusTest
@TestProfile(AppFillingProfile.OneAzApp.class)
public class AzWindowBasicsTest extends WebUiTest {
	
	@Test
	public void testWindowInit() {
		TestUser user = this.getTestUserService().getUser();
		Page page = this.newPage();
		PageLog pageLog = this.getLog(page);
		
		Pattern windowInitBannerLog = Pattern.compile("======== Initializing new Azide window frame:\nazideWindow");
		Pattern windowInitFinishedBannerLog = Pattern.compile("Finished initializing new Azide window frame:");
		
		pageLog.registerPattern(windowInitBannerLog, windowInitFinishedBannerLog);
		
		this.loginPage(page, user);
		
		pageLog.assertPatternHits(windowInitBannerLog, 1);
		pageLog.assertPatternHits(windowInitFinishedBannerLog, 1);
		
		String windowFrameId = pageLog.getPatternHits(windowInitFinishedBannerLog).get(0).split("\n")[1];
		
		log.info("Window frame id: {}", windowFrameId);
		
		assertNotNull(windowFrameId);
	}
	
	@Test
	public void testAppInit() {
		TestUser user = this.getTestUserService().getUser();
		Page page = this.newPage();
		PageLog pageLog = this.getLog(page);
		this.loginPage(page, user);
		
		Pattern windowInitFinishedBannerLog = Pattern.compile("Finished initializing new Azide window frame:");
		Pattern appInitBannerLog = Pattern.compile("======== Initializing new azide app:\ntest-service-0");
		Pattern appInitFinishedBannerLog = Pattern.compile("Finished initializing new Azide app frame:");
		Pattern appInitParentWindowLog = Pattern.compile("Parent window frame id:");
		
		pageLog.registerPattern(windowInitFinishedBannerLog, appInitBannerLog, appInitFinishedBannerLog, appInitParentWindowLog);
		
		//reload to force a fresh page/ trigger new init process
		page.reload();
		
		pageLog.assertPatternHits(windowInitFinishedBannerLog, 1);
		pageLog.assertPatternHits(appInitBannerLog, 1);
		pageLog.assertPatternHits(appInitFinishedBannerLog, 1);
		pageLog.assertPatternHits(appInitParentWindowLog, 1);
		
		String windowFrameId = pageLog.getPatternHits(windowInitFinishedBannerLog).get(0).split("\n")[1];
		log.info("Window frame id: {}", windowFrameId);
		assertNotNull(windowFrameId);
		
		String appFrameId = pageLog.getPatternHits(appInitFinishedBannerLog).get(0).split("\n")[1];
		log.info("App frame id: {}", appFrameId);
		assertNotNull(appFrameId);
		
		assertEquals(
			windowFrameId,
			pageLog.getPatternHits(appInitParentWindowLog).get(0).split("\n")[1]
		);
	}
	
	@Test
	public void testAppRoleCall() {
		TestUser user = this.getTestUserService().getUser();
		Page page = this.newPage();
		PageLog pageLog = this.getLog(page);
		this.loginPage(page, user);
		
		Pattern windowInitFinishedBannerLog = Pattern.compile("Finished initializing new Azide window frame:");
		Pattern appInitFinishedBannerLog = Pattern.compile("Finished initializing new Azide app frame:");
		
		Pattern roleCallCallLog = Pattern.compile(" Received new role call 'CALL' message:");
		Pattern roleCallHereLog = Pattern.compile(" Received new role call 'HERE' message:");
		
		pageLog.registerPattern(windowInitFinishedBannerLog, appInitFinishedBannerLog, roleCallCallLog, roleCallHereLog);
		
		//reload to force a fresh page/ trigger new init process
		page.reload();
		
		pageLog.assertPatternHits(windowInitFinishedBannerLog, 1);
		pageLog.assertPatternHits(appInitFinishedBannerLog, 1);
		pageLog.assertPatternHits(roleCallCallLog, 1);
		pageLog.assertPatternHits(roleCallHereLog, 1);
		
		String windowFrameId = pageLog.getPatternHits(windowInitFinishedBannerLog).get(0).split("\n")[1];
		log.info("Window frame id: {}", windowFrameId);
		assertNotNull(windowFrameId);
		
		String appFrameId = pageLog.getPatternHits(appInitFinishedBannerLog).get(0).split("\n")[1];
		log.info("App frame id: {}", appFrameId);
		assertNotNull(appFrameId);
		
		assertTrue(
			pageLog.getPatternHits(roleCallCallLog).get(0).contains(windowFrameId + roleCallCallLog.pattern())
		);
		assertTrue(
			pageLog.getPatternHits(roleCallHereLog).get(0).contains(appFrameId + roleCallHereLog.pattern())
		);
		
		String listInWindow = page.waitForFunction("azideWin.getIfc().getChannels().getOtherChannelNames()").jsonValue().toString();
		log.info("channel list in window: {}", listInWindow);
		assertEquals(
			"[test-service-0, "+appFrameId+"]",
			listInWindow
		);
		
		FrameLocator appFrame = page.frameLocator("#appframe");
		
		String listInApp = appFrame.locator(":root").evaluate("""
			JSON.stringify(azApp.getIfc().getChannels().getOtherChannelNames());
			""").toString();
		log.info("Result from running js in frame: {}", listInApp);
		assertEquals(
			"[\"azideWindow\",\""+windowFrameId+"\"]",
			listInApp
		);
	}
}
