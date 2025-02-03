package mil.army.dcgs.azide.testResources.profiles;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.HashMap;
import java.util.Map;

public abstract class AppFillingProfile {
	
	private static Map<String, String> getAppConfig(
		int numApps
	){
		Map<String, String> output = new HashMap<>();
		for (int i = 0; i < numApps; i++) {
			String ref = "test-service-" + i;
			String endpoint = "/test/app?testAppRef=" + ref;
			
			output.put("applicationInfo.applications["+i+"].name", "Test App " + i);
			output.put("applicationInfo.applications["+i+"].description", "Test App " + i + " description.");
			output.put("applicationInfo.applications["+i+"].reference", ref);
			output.put("applicationInfo.applications["+i+"].location", endpoint);
			output.put("applicationInfo.applications["+i+"].showInAppBar", "true");
			
			if(i == 0){
				output.put("applicationInfo.applications["+i+"].defaultApp", "true");
				output.put("applicationInfo.applications["+i+"].splashApp", "true");
				output.put("applicationInfo.applications["+i+"].splashEndpoint", endpoint);
			}
		}
		return output;
	}
	
	private static abstract class Napps implements QuarkusTestProfile {
		
		abstract int getNumApps();
		
		@Override
		public Map<String, String> getConfigOverrides() {
			return AppFillingProfile.getAppConfig(this.getNumApps());
		}
	}
	
	public static class OneApp extends Napps {
		@Override
		int getNumApps() {
			return 1;
		}
	}
	
	public static class TwoApps extends Napps {
		@Override
		int getNumApps() {
			return 2;
		}
	}
	
	public static class ManyApps extends Napps {
		@Override
		int getNumApps() {
			return 20;
		}
	}
}
