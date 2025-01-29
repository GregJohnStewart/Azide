package mil.army.dcgs.azide.testResources.testClasses;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.testUser.TestUser;
import mil.army.dcgs.azide.testResources.ui.PlaywrightSetup;
import mil.army.dcgs.azide.testResources.ui.utilities.NavUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;

@Slf4j
@Tag("ui")
public class WebUiTest {
	
	private static Path getCurTestDir(TestInfo testInfo) {
		log.debug("Display name: {}", testInfo.getDisplayName());
		
		Path output = PlaywrightSetup.RECORDINGS_DIR.resolve(testInfo.getTestClass().get().getName());
		
		if (testInfo.getDisplayName().startsWith("[")) {
			return output.resolve(testInfo.getTestMethod().get().getName())
					   .resolve(testInfo.getDisplayName().replaceAll("/", ""));
		} else {
			return output.resolve(testInfo.getDisplayName().replaceAll("\\(\\)", ""));
		}
	}
	
	protected static Page.ScreenshotOptions getScreenshotOptions() {
		return new Page.ScreenshotOptions().setFullPage(true);
	}
	
	@Getter
	private BrowserContext context;
	
	@Getter
	private Path curTestUiResultDir;
	
	@BeforeEach
	public void beforeEachUi(TestInfo testInfo) {
		this.curTestUiResultDir = getCurTestDir(testInfo);
		Browser.NewContextOptions newContextOptions = new Browser.NewContextOptions()
														  .setRecordVideoDir(this.curTestUiResultDir)
														  .setScreenSize(1920, 1080)
														  .setRecordVideoSize(1920, 1080)
														  .setViewportSize(1920, 1080);
		
		this.context = PlaywrightSetup.getInstance().getBrowser().newContext(newContextOptions);
	}
	
	@AfterEach
	public void afterEachUi(TestInfo testInfo) throws InterruptedException, IOException {
		
		for (int i = 0; i < this.getContext().pages().size(); i++) {
			Page curPage = this.getContext().pages().get(i);
			Path curPageFinalScreenshot = this.curTestUiResultDir.resolve("page-" + (i + 1) + "-final.png");
			Path curPageHtmlFile = this.curTestUiResultDir.resolve("page-" + (i + 1) + "-final-code.html");
			Path curPageInfoFile = this.curTestUiResultDir.resolve("page-" + (i + 1) + "-final-info.txt");
			
			curPage.screenshot(getScreenshotOptions().setPath(curPageFinalScreenshot));
			try (OutputStream outputStream = new FileOutputStream(curPageHtmlFile.toFile())) {
				outputStream.write(curPage.content().getBytes());
			}
			try (
				OutputStream outputStream = new FileOutputStream(curPageInfoFile.toFile());
				PrintWriter pw = new PrintWriter(outputStream);
			) {
				pw.println("Test info:");
				pw.println();
				pw.println("Final url: " + curPage.url());
				pw.println();
			}
		}
		Thread.sleep(250);
		this.context.close();
	}
	
	protected Page getLoggedInPage(TestUser user, String page) {
		Page output = this.getContext().newPage();
		
		NavUtils.navigateToEndpoint(output, page);
		
		if (output.title().contains("Sign in")) {
			log.info("Need to log in user.");
			
			Locator locator = output.locator("#password");
			locator.fill(user.getPassword());
			locator = output.locator("#username");
			locator.fill(user.getName());
			
			output.locator("#kc-login").click();
			output.waitForLoadState();
			
			if (!output.locator("#navLogo").isVisible()) {
				throw new IllegalStateException("Not logged in.");
			}
			log.info("Logged in user: " + user);
		} else {
			log.info("Was already logged in?");
		}
		
		return output;
	}

}
