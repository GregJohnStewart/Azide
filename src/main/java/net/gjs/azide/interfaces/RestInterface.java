package net.gjs.azide.interfaces;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.gjs.azide.entities.model.Person;
import net.gjs.azide.service.PersonRepository;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;

@Slf4j
public abstract class RestInterface {
    @Getter(AccessLevel.PROTECTED)
    @Inject
    JsonWebToken accessToken;

    @Getter(AccessLevel.PROTECTED)
    @Inject
    PersonRepository personRepository;

    @Getter(AccessLevel.PROTECTED)
    @Context
    SecurityContext securityContext;

    @Getter(AccessLevel.PROTECTED)
    Person user = null;

    protected boolean hasAccessToken(){
        return this.getAccessToken() != null && this.getAccessToken().getClaimNames() != null;
    }

    /**
     * When hit from bare API call with just bearer, token will be access token.
     *
     * When hit from ui, idToken.
     * @return
     */
    protected JsonWebToken getUserToken(){
        if(this.hasAccessToken()){
            return this.getAccessToken();
        }
        return null;
    }

    private Optional<Person> logRequestAndProcessEntity() {
        log.debug("JWT: {}", this.getAccessToken());
        if (this.getSecurityContext().isSecure()) {
            log.warn("Request with Auth made without HTTPS");
        }

        this.user = this.getPersonRepository().ensurePerson(this.getSecurityContext(), this.getUserToken());

        log.info("Processing request with Auth; interacting entity: {}", user);
        return Optional.of(this.user);
    }

    protected Person requireAndGetEntity() {
        Person entity = this.getUser();

        if(entity == null){
            //TODO:: review this
            throw new ForbiddenException("Required to have auth. No entity.");
        }

        return entity;
    }

    @PostConstruct
    void initialLogAndEntityProcess(){
        this.logRequestAndProcessEntity();
    }
}
