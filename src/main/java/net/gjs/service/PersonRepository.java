package net.gjs.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import net.gjs.model.entities.Person;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public Person findByName(String name){
        return find("name", name).firstResult();
    }
}