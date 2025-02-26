package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.entities.model.Profile;
import mil.army.dcgs.azide.entities.model.FavoriteApp;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

@Slf4j
@Named("ProfileRepository")
@ApplicationScoped
public class ProfileRepository implements PanacheRepository<Profile> {

    public Optional<Profile> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }

    public Optional<Profile> findByUsername(JsonWebToken jwt){
        return this.findByUsername(jwt.getTokenID());
    }

    public Profile ensureProfile(SecurityContext context, JsonWebToken jwt) {
        // Use the username since it will be unique and wont change based on the generation of the
        // JsonWebToken or SecurityContext.
        String username = context.getUserPrincipal().getName();

        // Get the Profile object from the database based on the unique identifier.
        Optional<Profile> gotten = this.findByUsername(username);

        ArrayList<FavoriteApp> favorites = new ArrayList<FavoriteApp>();
        // TEST ============================================
        FavoriteApp app = FavoriteApp.builder()
            .name("Landing Page")
            .xLocation(0)
            .yLocation(0)
            .windowWidth(800)
            .windowHeight(800)
            .build();
        favorites.add(app);
        app = FavoriteApp.builder()
            .name("Test Data Viewer")
            .xLocation(0)
            .yLocation(0)
            .windowWidth(800)
            .windowHeight(800)
            .build();
        favorites.add(app);

        // TEST ============================================

        Profile output;
        output = gotten.orElseGet(() -> {
            Profile newProfile = Profile.builder()
                    .username(username)
                    .favorites(favorites)
                    .build();
            this.persist(newProfile);
            log.info("Created new profile: {}", newProfile);
            return newProfile;
        });

        return output;
    }

    public List<Profile> getProfiles(){
        List<Profile> profiles = this.findAll().stream().toList();

        ArrayList<Profile> sortedProfiles = new ArrayList<Profile>(profiles);

        Collections.sort(sortedProfiles,
            Comparator.comparing(Profile::getUsername));

        return sortedProfiles;
    }

    public void addFavoriteApp(Profile profile, String favoriteName) {
        // TODO Make sure the favorite app being added does not already exist.

        FavoriteApp favorite = FavoriteApp.builder()
            .name(favoriteName)
            .xLocation(0)
            .yLocation(0)
            .windowWidth(800)
            .windowHeight(400)
            .build();

        profile.getFavorites().add(favorite);
        this.persist(profile);
    }

    public void deleteFavoriteApp(Profile profile, String favoriteName) {
        // Remove the favorite app that matches the given favoriteId
        boolean removed = profile.getFavorites().removeIf(fav -> fav.getName().equals(favoriteName));
        
        if (removed) {
            // Persist the updated profile. Since the Profile is attached and the
            // favorites collection has orphanRemoval enabled, the removed FavoriteApp
            // will be deleted from the database.
            this.persist(profile);
        }
    }
}