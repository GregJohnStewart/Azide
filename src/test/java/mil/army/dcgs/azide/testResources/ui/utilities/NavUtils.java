package mil.army.dcgs.azide.testResources.ui.utilities;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.ui.assertions.MainAssertions;
import org.eclipse.microprofile.config.ConfigProvider;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class NavUtils {
	
	private static URI INDEX = null;
	public static void init(URI index) {
		if (INDEX == null) {
			INDEX = index;
		}
	}
	public static URI getIndex() {
		
		return INDEX;
	}
	
	@FunctionalInterface
	public interface OtherPageFunction<T> {
		T doThing(Page page);
	}
	
	public static <T> T performOnOtherPage(
		Page page,
		String otherPageEndpoint,
		OtherPageFunction<T> otherPageFunction
	){
		String originalPage = page.url();
		
		navigateToEndpoint(page, otherPageEndpoint);
		
		T output = otherPageFunction.doThing(page);
		
		log.info("Returning to original page: {}", originalPage);
		navigateToUrl(page, originalPage);
		
		return output;
	}
	
	public static Page navigateToUrl(Page page, String url){
		log.info("Navigating to {}", url);
		Response response = page.navigate(url);
		
		assertEquals("OK", response.statusText());
		MainAssertions.assertDoneProcessing(page);
		log.debug("Done navigating to {}", url);
		return page;
	}
	
	public static Page navigateToEndpoint(Page page, String endpoint){
		if(endpoint.startsWith("/")){
			endpoint = endpoint.substring(1);
		}
		
		String url = getIndex().toString() + endpoint;
		return navigateToUrl(page, url);
	}
	
	
	
	
}
