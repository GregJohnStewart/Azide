package mil.army.dcgs.azide.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.testResources.testClasses.WebServerTest;
import net.datafaker.Faker;
import mil.army.dcgs.azide.entities.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@QuarkusTest
public class PersonRepositoryTest extends WebServerTest {

    @Inject
    PersonRepository personRepository;

    @Inject
    ObjectMapper objectMapper;

    @Test
    @Transactional
    public void simpleCreationTest(){
        Person person = new Person();
        person.setName(FAKER.name().fullName());
        person.setExternalId(FAKER.idNumber().valid());

        this.personRepository.persist(person);
        log.info("Person : {}", person);
        assertNotNull(person.getId());

        Person fromDb = this.personRepository.find("id", person.getId()).firstResult();

        assertEquals(person, fromDb);
    }

    @Test
    @Transactional
    public void updateTest(){
        Person person = new Person();
        person.setName(FAKER.name().fullName());
        person.setExternalId(FAKER.idNumber().valid());

        this.personRepository.persist(person);
        log.info("Person : {}", person);

        person.setName(FAKER.name().fullName());
        this.personRepository.persist(person);
        assertNotNull(person.getId());

        Person fromDb = this.personRepository.find("id", person.getId()).firstResult();

        assertEquals(person, fromDb);
    }

    @Test
    @Transactional
    public void serializationTest() throws JsonProcessingException {
        Person person = new Person();
        person.setName(FAKER.name().fullName());
        person.setExternalId(FAKER.idNumber().valid());

        this.personRepository.persist(person);
        log.info("Person : {}", person);

        this.personRepository.persist(person);

        Person fromDb = this.personRepository.find("id", person.getId()).firstResult();

        String json = this.objectMapper.writeValueAsString(fromDb);
    }

}
