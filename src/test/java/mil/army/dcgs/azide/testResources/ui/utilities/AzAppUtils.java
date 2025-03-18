package mil.army.dcgs.azide.testResources.ui.utilities;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Page;

public class AzAppUtils {
	
	public static FrameLocator getAzAppFrame(Page page) {
		return page.frameLocator("#appframe");
	}
	
	public static String runInAppFrame(Page page, String expression) {
		return getAzAppFrame(page).locator(":root").evaluate(expression).toString();
	}
	
	public static String runInWindow(Page page, String expression){
		return page.evaluate(expression).toString();
	}
	
	public static String getAzWinFrameId(Page page) {
		return runInWindow(page,"""
			azideWin.getFrameId();
			""" );
	}
	
	public static String getAzAppFrameId(Page page) {
		return runInAppFrame(
			page,
			"""
				azApp.getFrameId();
				"""
		);
	}
}
