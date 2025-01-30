package mil.army.dcgs.azide.testResources.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.smallrye.config.WithDefault;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.GenericContainer;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class MessagesAppTestResource implements QuarkusTestResourceLifecycleManager {
	private static final String MESSAGE_SERVICE_IMAGE ="dcgs/message-service:1.0.0-SNAPSHOT";
	
	GenericContainer<?> messagesAppContainer;
	
	@Override
	public Map<String, String> start() {
		int port = 8080;
		this.messagesAppContainer = new GenericContainer<>(MESSAGE_SERVICE_IMAGE)
										.withExposedPorts(port)
//										.withAccessToHost(true)
										.withEnv("QUARKUS_DATASOURCE_JDBC_URL", "jdbc:sqlite::memory:")
										.withEnv("priorityMessages.messages[0].title", "test Message")
										.withEnv("priorityMessages.messages[0].priority", "1")
										.withEnv("priorityMessages.messages[0].content", "test Message!")
										;
		this.messagesAppContainer.start();
		
		String url = "http://" + this.messagesAppContainer.getHost() + ":" + this.messagesAppContainer.getMappedPort(port);
		
		log.info("Messages app url: {}", url);
		
		return Map.of(
			"applicationInfo.applications[0].name", "Message Service",
			"applicationInfo.applications[0].reference", "message-service",
			"applicationInfo.applications[0].description", "Message service to alert users.",
			"applicationInfo.applications[0].location", url,
			"applicationInfo.applications[0].showInAppBar", "true",
			"applicationInfo.applications[0].defaultApp", "true",
			"applicationInfo.applications[0].splashApp", "true",
			"applicationInfo.applications[0].splashEndpoint", url
		);
	}
	
	@Override
	public void stop() {
		if(this.messagesAppContainer != null) {
			this.messagesAppContainer.stop();
			this.messagesAppContainer = null;
		}
	}
}
