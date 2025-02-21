package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.entities.model.Person;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;
import java.util.ArrayList;

@Slf4j
@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public Optional<Person> findByExternalId(String id){
        return find("externalId", id).firstResultOptional();
    }

    public Optional<Person> findByExternalId(JsonWebToken jwt){
        return this.findByExternalId(jwt.getIssuer());
    }

    public Person ensurePerson(SecurityContext context, JsonWebToken jwt){
        String externalId = jwt.getIssuer();
        //TODO:: better
        Optional<Person> gotten = this.findByExternalId(externalId);

        Person output;
        output = gotten.orElseGet(() -> {
            Person newUser = Person.builder()
                    .externalId(externalId)
                    .name(context.getUserPrincipal().getName())
                    .favorites(new ArrayList<String>())
                    .build();
            this.persist(newUser);
            log.info("Created new user: {}", newUser);
            return newUser;
        });

        return output;
    }
}