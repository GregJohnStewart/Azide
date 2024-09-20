package net.gjs.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.SecurityContext;
import net.gjs.model.entities.Person;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public Optional<Person> findByExternalId(String id){
        return find("externalId", id).firstResultOptional();
    }

    public Person ensurePerson(SecurityContext context, JsonWebToken jwt){
        String externalId = jwt.getIssuer();
        //TODO:: better
        Optional<Person> gotten = this.findByExternalId(externalId);

        Person output;
        output = gotten.orElseGet(() -> Person.builder()
                .externalId(externalId)
                .name(context.getUserPrincipal().getName())
                .build());

        return output;
    }
}