package mil.army.dcgs.azide.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import mil.army.dcgs.azide.entities.model.CustomSite;
import mil.army.dcgs.azide.entities.model.Person;
import mil.army.dcgs.azide.service.PersonRepository;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@QuarkusTest
public class PersonRepositoryTest {
    private static final Faker FAKER = new Faker();

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
    public void addCustomSiteTest(){
        Person person = new Person();
        person.setName(FAKER.name().fullName());
        person.setExternalId(FAKER.idNumber().valid());

        this.personRepository.persist(person);
        log.info("Person : {}", person);


        CustomSite newCustomSite = new CustomSite();
        newCustomSite.setTitle(FAKER.book().title());
        newCustomSite.setDescription(FAKER.lorem().paragraph());
        newCustomSite.setUri(URI.create(FAKER.internet().url()));
        newCustomSite.setPerson(person);

//        this.customSiteRepository.persist(newCustomSite);
        person.getCustomSites().add(newCustomSite);
        this.personRepository.persist(person);

        Person fromDb = this.personRepository.find("id", person.getId()).firstResult();
        assertFalse(fromDb.getCustomSites().isEmpty());
//        assertEquals(person, fromDb);
        log.info("Original custom site: {}", newCustomSite);
        assertNotNull(newCustomSite.getId());
        assertEquals(fromDb, newCustomSite.getPerson());
        log.info("Custom sites: {}", fromDb.getCustomSites());
        CustomSite fromPersonFromDb = fromDb.getCustomSites().getFirst();
        assertNotNull(fromPersonFromDb);
        assertNotNull(fromPersonFromDb.getPerson());
        assertEquals(newCustomSite.getId(), fromPersonFromDb.getId());
        assertEquals(fromDb, fromPersonFromDb.getPerson());
        assertNotNull(fromPersonFromDb.getId());
    }

    @Test
    @Transactional
    public void serializationTest() throws JsonProcessingException {
        Person person = new Person();
        person.setName(FAKER.name().fullName());
        person.setExternalId(FAKER.idNumber().valid());

        this.personRepository.persist(person);
        log.info("Person : {}", person);


        CustomSite newCustomSite = new CustomSite();
        newCustomSite.setTitle(FAKER.book().title());
        newCustomSite.setDescription(FAKER.lorem().paragraph());
        newCustomSite.setUri(URI.create(FAKER.internet().url()));
        newCustomSite.setPerson(person);

//        this.customSiteRepository.persist(newCustomSite);
        person.getCustomSites().add(newCustomSite);
        this.personRepository.persist(person);

        Person fromDb = this.personRepository.find("id", person.getId()).firstResult();

        String json = this.objectMapper.writeValueAsString(fromDb);
    }

}
