package mil.army.dcgs.azide.interfaces;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;

import mil.army.dcgs.azide.entities.model.Profile;

import mil.army.dcgs.azide.service.ProfileRepository;

@Slf4j
public abstract class RestInterface {
    @Getter(AccessLevel.PROTECTED)
    @Inject
    JsonWebToken accessToken;

    @Getter(AccessLevel.PROTECTED)
    @Inject
    ProfileRepository profileRepository;

    @Getter(AccessLevel.PROTECTED)
    @Context
    SecurityContext securityContext;

    Profile profile = null;

    protected Profile getProfile(){
        if(this.profile == null){
            this.profile = this.getProfileRepository().ensureProfile(this.getSecurityContext(), this.getUserToken());
        }
        return this.profile;
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
