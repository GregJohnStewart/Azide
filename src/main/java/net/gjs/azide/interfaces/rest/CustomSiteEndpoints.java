package net.gjs.azide.interfaces.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import net.gjs.azide.entities.model.CustomSite;
import net.gjs.azide.entities.model.Person;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequestScoped
@Path(RestInterface.BASE_PATH + "/customSite")
public class CustomSiteEndpoints extends RestInterface {

//    @Inject
//    CustomSiteRepository customSiteRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public @NotNull List<CustomSite> get() {
        log.info("Getting user's custom sites.");
        List<CustomSite> output = this.getFullUser().getCustomSites();
        log.debug("Got {} sites", output.size());

        return output;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public @NotNull CustomSite create(ObjectNode newSiteJson) {
        log.info("Creating a new custom site: {}", newSiteJson);
        Person user = this.getFullUser();

        CustomSite newSite;
        try {
            newSite = CustomSite.builder()
                    .person(user)
                    .title(newSiteJson.get("title").asText())
                    .description(newSiteJson.get("description").asText())
                    .uri(new URI(newSiteJson.get("uri").asText()))
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL given.", e);
        }
        log.debug("New site (pre persist): {}", newSite);


        user.getCustomSites().add(newSite);
        this.getPersonRepository().persist(user);

        log.debug("Created new site!");
        return newSite;
    }

    @PostConstruct
    public void debug(){
        log.info("debug!");
    }
}
