package mil.army.dcgs.azide.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.testClasses.WebServerTest;
import mil.army.dcgs.azide.entities.model.Profile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@QuarkusTest
public class ProfileRepositoryTest extends WebServerTest {

    @Inject
    ProfileRepository profileRepository;

    @Inject
    ObjectMapper objectMapper;

    @Test
    @Transactional
    public void simpleCreationTest(){
        Profile profile = new Profile();
        profile.setUsername(FAKER.name().fullName());

        this.profileRepository.persist(profile);
        log.info("profile : {}", profile);
        assertNotNull(profile.getId());

        Profile fromDb = this.profileRepository.find("id", profile.getId()).firstResult();

        assertEquals(profile, fromDb);
    }

    @Test
    @Transactional
    public void updateTest(){
        Profile profile = new Profile();
        profile.setUsername(FAKER.name().fullName());

        this.profileRepository.persist(profile);
        log.info("profile : {}", profile);

        profile.setUsername(FAKER.name().fullName());
        this.profileRepository.persist(profile);
        assertNotNull(profile.getId());

        Profile fromDb = this.profileRepository.find("id", profile.getId()).firstResult();

        assertEquals(profile, fromDb);
    }

    @Test
    @Transactional
    public void serializationTest() throws JsonProcessingException {
        Profile profile = new Profile();
        profile.setUsername(FAKER.name().fullName());

        this.profileRepository.persist(profile);
        log.info("profile : {}", profile);

        this.profileRepository.persist(profile);

        Profile fromDb = this.profileRepository.find("id", profile.getId()).firstResult();

        String json = this.objectMapper.writeValueAsString(fromDb);
    }

}
