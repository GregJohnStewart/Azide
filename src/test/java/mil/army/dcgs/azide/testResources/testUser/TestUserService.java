package mil.army.dcgs.azide.testResources.testUser;

import java.util.HashMap;
import java.util.Map;

public class TestUserService {
	
	private static final TestUserService INTANCE = new TestUserService();
	
	public static TestUserService getInstance() {
		return INTANCE;
	}
	
	private Map<UserType, TestUser> users;
	
	private TestUserService() {
		users = new HashMap<>();
		users.put(
			UserType.NORMAL,
			TestUser.builder()
				.name("alice")
				.password("alice")
				.build()
		);
	}
	
	public TestUser getUser(UserType userType) {
		return users.get(userType);
	}
	
	public TestUser getUser(){
		return getUser(UserType.NORMAL);
	}
	
	
	public static enum UserType {
		NORMAL,
		ADMIN
	}
}
