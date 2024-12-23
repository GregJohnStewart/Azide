package net.gjs.azide.interfaces;

import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
import net.gjs.azide.entities.model.ClassificationBanner;
import net.gjs.azide.service.ClassificationRepository;

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
    
    @Getter(AccessLevel.PRIVATE)
    ClassificationRepository classificationRepository;

    @Getter(AccessLevel.PRIVATE)
    Person user = null;

    protected Person getFullUser(){
        if(this.getUser() == null){
            this.user = this.getPersonRepository().ensurePerson(this.getSecurityContext(), this.getUserToken());
        }

        return this.getUser();
    }
    
    protected ClassificationBanner getClassificationBanner(){
        
        return this.getClassificationRepository().findAll().list().getFirst();
    }

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
}
