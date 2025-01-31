package mil.army.dcgs.azide.testResources.testUser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestUser {
	private String name;
	private String password;
}
