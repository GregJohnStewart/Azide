package mil.army.dcgs.azide.testResources.lifecycle;

import mil.army.dcgs.azide.testResources.ui.PlaywrightSetup;
import org.apache.commons.io.FileUtils;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.io.IOException;

public class UiTestCleanup implements TestExecutionListener {
	
	@Override
	public void testPlanExecutionStarted(TestPlan testPlan) {
		TestExecutionListener.super.testPlanExecutionStarted(testPlan);
		
		try {
			FileUtils.deleteDirectory(PlaywrightSetup.RECORDINGS_DIR.toFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
		TestExecutionListener.super.executionFinished(testIdentifier, testExecutionResult);
		
		PlaywrightSetup.getInstance().close();
	}

}
