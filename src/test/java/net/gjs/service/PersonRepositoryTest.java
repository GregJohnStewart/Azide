package net.gjs.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import net.gjs.model.entities.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@QuarkusTest
public class PersonRepositoryTest {
    private static final Faker FAKER = new Faker();

    @Inject
    PersonRepository personRepository;

    @Test
    @Transactional
    public void simpleCreationTest(){
        Person person = new Person();
        person.setName(FAKER.name().fullName());

        this.personRepository.persist(person);
        log.info("Person : {}", person);
        assertNotNull(person.getId());
    }

}
