package mil.army.dcgs.azide.testResources.ui;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.Closeable;
import java.nio.file.Path;

@Slf4j
public class PlaywrightSetup implements Closeable {
	
	public static final Path RECORDINGS_DIR = Path.of("target/test-results/" + ConfigProvider.getConfig().getValue("quarkus.profile", String.class) + "/ui/");
	private static PlaywrightSetup INSTANCE = null;
	
	public static synchronized PlaywrightSetup getInstance() {
		if (INSTANCE == null || INSTANCE.isClosed()) {
			INSTANCE = new PlaywrightSetup();
		}
		return INSTANCE;
	}
	
	@Getter
	private final Playwright playwright;
	@Getter
	private final Browser browser;
	@Getter
	private boolean closed = false;
	
	{
		log.info("Setting up playwright for UI tests.");
		try {
			this.playwright = Playwright.create();
		} catch(Throwable e) {
			log.error("Failed creating new playwright: ", e);
			throw e;
		}
		//TODO:: choose browser based on config
		this.browser = this.playwright.firefox().launch();
		
		log.info("DONE setting up playwright.");
	}
	
	@Override
	public void close() {
		if (this.isClosed()) {
			return;
		}
		log.info("Cleaning up playwright.");
		browser.close();
		playwright.close();
		log.info("DONE cleaning up playwright.");
		this.closed = true;
	}
}
