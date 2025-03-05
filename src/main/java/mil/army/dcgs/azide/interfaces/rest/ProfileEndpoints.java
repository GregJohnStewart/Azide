package mil.army.dcgs.azide.interfaces.rest;

import java.time.LocalDateTime;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

import mil.army.dcgs.azide.entities.model.Profile;
import mil.army.dcgs.azide.entities.model.FavoriteApp;
import mil.army.dcgs.azide.service.ProfileRepository;

/**
 * TODO:: rework
 */
@Slf4j
@RequestScoped
@Path("/api/profile")
@Authenticated //TODO:: use Roles
public class ProfileEndpoints extends RestInterface {
	
	@Inject
	ProfileRepository profileRepository;
	
	@Inject
	Template profiletablefragment; // Injects the Qute template named "profiletablefragment.html"
	
	@Inject
	Template favoriteiconfragment; // Injects the Qute template named "favoriteiconfragment.html"
	
	@Context
	SecurityContext securityContext;
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public @NotNull List<Profile> getAllProfiles() {
		List<Profile> output = profileRepository.getProfiles();
		
		return output;
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public @NotNull Profile createProfile(ObjectNode profileJson) {
		Profile newProfile;
		
		newProfile = Profile.builder()
						 .username(profileJson.get("username").asText())
						 .favorites(new ArrayList<FavoriteApp>())
						 .build();
		
		log.debug("New profile (pre persist): {}", newProfile);
		
		this.getProfileRepository().persist(newProfile);
		log.debug("Created new profile!");
		return newProfile;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public @NotNull Profile updateProfile(
		@NotNull @PathParam("id") UUID id,
		ObjectNode profileJson
	) {
		
		log.info("Updating a profile: {}", profileJson);
		
		Profile profile = this.profileRepository.find("id", id).firstResultOptional()
							  .orElseThrow(NotFoundException::new);
		
		profile.setUsername(profileJson.get("username").asText());
		profile.setLastSeen(LocalDateTime.now());//TODO:: move updating this to
		
		// TODO Update to add favorites too? or make this a separate call?
		
		profile.persist();
		
		return profile;
	}
	
	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public @NotNull Profile deleteProfile(@NotNull @PathParam("id") UUID id) {
		log.info("Deleting a Profile: {}", id);
		
		Profile profile = this.profileRepository.find("id", id).firstResultOptional()
							  .orElseThrow(NotFoundException::new);
		
		this.profileRepository.delete(profile);
		
		return profile;
	}
	
	@GET
	@Path("/profile-table")
	@Produces(MediaType.TEXT_HTML)
	public TemplateInstance getProfileTable() {
		List<Profile> profiles = profileRepository.getProfiles();
		return profiletablefragment.data("profiles", profiles);
	}
	
	@PUT
	@Path("/favorite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public @NotNull Profile addFavorite(
		ObjectNode favoriteNameJson
	) {
		Profile profile = this.profileRepository.ensureProfile(securityContext, null);
		
		if (!profile.isFavoriteSet(favoriteNameJson.get("name").asText())) {
			profile = this.profileRepository.addFavoriteApp(profile, favoriteNameJson.get("name").asText());
		}
		
		return profile;
	}
	
	@DELETE
	@Path("/favorite")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public @NotNull Profile deleteFavorite(
		ObjectNode favoriteNameJson
	) {
		Profile profile = this.profileRepository.ensureProfile(securityContext, null);
		
		if (profile.isFavoriteSet(favoriteNameJson.get("name").asText())) {
			profile = this.profileRepository.deleteFavoriteApp(profile, favoriteNameJson.get("name").asText());
		}
		
		return profile;
	}
}
