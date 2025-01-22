package mil.army.dcgs.azide.interfaces;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

import mil.army.dcgs.azide.entities.model.Person;

import mil.army.dcgs.azide.service.PersonRepository;

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
    Person user = null;

    protected Person getFullUser(){
        if(this.getUser() == null){
            this.user = this.getPersonRepository().ensurePerson(this.getSecurityContext(), this.getUserToken());
        }

        return this.getUser();
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
