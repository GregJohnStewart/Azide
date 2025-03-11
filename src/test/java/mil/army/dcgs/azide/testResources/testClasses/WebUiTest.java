package mil.army.dcgs.azide.testResources.testClasses;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.ConsoleMessage;
import com.microsoft.playwright.JSHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.testUser.TestUser;
import mil.army.dcgs.azide.testResources.testUser.TestUserService;
import mil.army.dcgs.azide.testResources.ui.PlaywrightSetup;
import mil.army.dcgs.azide.testResources.ui.assertions.MainAssertions;
import mil.army.dcgs.azide.testResources.ui.pages.AupPage;
import mil.army.dcgs.azide.testResources.ui.pages.IndexPage;
import mil.army.dcgs.azide.testResources.ui.pages.KeycloakUi;
import mil.army.dcgs.azide.testResources.ui.utilities.NavUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Tag("ui")
public class WebUiTest extends RunningServerTest {
	
	private final Map<Integer, PageLog> pageLogs = new HashMap<>();
	
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
	
	@Getter
	private TestUserService testUserService = TestUserService.getInstance();
	
	@BeforeEach
	public void beforeEachUi(TestInfo testInfo) throws URISyntaxException {
		NavUtils.init(this.getIndex().toURI());
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
//			try (
//				OutputStream outputStream = new FileOutputStream(curPageConsoleFile.toFile());
//				PrintWriter pw = new PrintWriter(outputStream);
//			) {
//				for (ConsoleMessage consoleMessage : console) {
//					pw.println(consoleMessage.text());
//				}
//			}
			for(PageLog curLog : this.pageLogs.values()){
				curLog.close();
			}
			this.pageLogs.clear();
		}
		Thread.sleep(250);
		this.context.close();
	}
	
	public PageLog getLog(Page page){
		return this.pageLogs.get(this.getContext().pages().indexOf(page));
	}
	
	public Page newPage(){
		log.info("Getting new page in window.");
		Page output = this.getContext().newPage();
		int pageIndex = this.getContext().pages().indexOf(output);
		Path curPageConsoleFile = this.curTestUiResultDir.resolve("page-" + (pageIndex + 1) + "-console.log");
		PageLog logs;
		try {
			logs = new PageLog(curPageConsoleFile);
		} catch(FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		this.pageLogs.put(pageIndex, logs);
		
		output.onPageError(logs::logError);
		output.onConsoleMessage(logs::log);
		
		return output;
	}
	
	protected Page getLoggedInPage(TestUser user, String page) {
		log.debug("Getting new logged in page.");
		Page output = this.newPage();
		this.loginPage(output, user, page);
		return output;
	}
	
	protected Page loginPage(Page page, TestUser user, String destination){
		log.debug("Getting new logged in page; Navigating to /");
		NavUtils.navigateToEndpoint(page, "/");
		
		page.locator(IndexPage.LOGIN_LINK).click();
		log.debug("Getting new logged in page: Clicked login link.");
		
		MainAssertions.assertDoneProcessing(page);
		
		
		if (page.title().contains("Sign in")) {
			log.debug("Getting new logged in page: Not signed in yet.");
			KeycloakUi.loginUser(user, page);
			
			page.locator(AupPage.AUP_ACCEPT_BUTTON).click();
			
			MainAssertions.assertDoneProcessing(page);
		} else {
			log.info("Was already logged in?");
		}
		
		if(destination != null){
			NavUtils.navigateToEndpoint(page, destination);
		}
		
		log.debug("Done getting logged in page.");
		
		return page;
	}
	
	protected Page loginPage(Page page, TestUser user){
		return this.loginPage(page, user, null);
	}
	
	protected Page getLoggedInPage(TestUser user){
		return this.getLoggedInPage(user, null);
	}
	
	@Data
	public static class PageLog implements Closeable {
		
		private Path logPath;
		private OutputStream logOutputStream;
		private Map<Pattern, List<String>> searchPatterns = new HashMap<>();
		
		public PageLog(Path logPath) throws FileNotFoundException {
			this.logPath = logPath;
			this.logOutputStream = new FileOutputStream(this.logPath.toFile());
		}
		
		public void registerPattern(Pattern pattern){
			this.searchPatterns.put(pattern, new ArrayList<>());
		}
		
		public int patternHits(Pattern pattern){
			return this.searchPatterns.get(pattern).size();
		}
		
		public void assertPatternHits(Pattern pattern, int hits){
			assertEquals(
				hits,
				this.patternHits(pattern)
			);
		}
		
		public void log(String message){
			try {
				this.logOutputStream.write(message.getBytes());
			} catch (IOException e) {
				log.error("Failed to write log message to file ", e);
			}
			
			for(Map.Entry<Pattern, List<String>> entry : this.searchPatterns.entrySet()){
				Pattern pattern = entry.getKey();
				List<String> hits = entry.getValue();
				
				if(pattern.matcher(message).find()){
					hits.add(message);
				}
			}
		}
		
		public void log(ConsoleMessage consoleMessage){
			StringBuilder message = new StringBuilder();
			try {
				if (!consoleMessage.args().isEmpty()) {
					boolean first = true;
					for (JSHandle curHandle : consoleMessage.args()) {
						if (first) {
							first = false;
							message.append("\t");
						}
						Object jsonValue = curHandle.jsonValue();
						message.append(
							jsonValue == null?
								"null":
							jsonValue.toString().strip()
						).append("\n");
					}
				}
				
				String output = String.format(
					"[%s][%s] %s\n",
					consoleMessage.location(),
					consoleMessage.type(),
					message
				);
				
				this.log(output);
			}catch(PlaywrightException|NullPointerException e){
				log.warn("FAILED to build log message: ", e);
			}
		}
		public void logError(String message){
			this.log(String.format(
				"\n\nERROR on page: %s\n\n",
				message
			));
		}
		
		@Override
		public void close() throws IOException {
			if(this.logOutputStream != null){
				this.logOutputStream.close();
				this.logOutputStream = null;
			}
		}
	}
}
