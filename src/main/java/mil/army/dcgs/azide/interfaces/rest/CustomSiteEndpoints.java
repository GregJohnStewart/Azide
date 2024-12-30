package mil.army.dcgs.azide.interfaces.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.entities.model.CustomSite;
import mil.army.dcgs.azide.entities.model.Person;
import mil.army.dcgs.azide.service.CustomSiteRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequestScoped
@Path(RestInterface.BASE_PATH + "/app/customSite")
public class CustomSiteEndpoints extends RestInterface {

    @Inject
    CustomSiteRepository customSiteRepository;

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

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public @NotNull CustomSite update(
            @NotNull @PathParam("id") UUID id,
            ObjectNode newSiteJson
    ) throws URISyntaxException {
        log.info("Updating a custom site: {}", newSiteJson);

        CustomSite site = this.customSiteRepository.find("id", id).firstResultOptional()
                .orElseThrow(NotFoundException::new);

        if(!site.getPerson().equals(this.getFullUser())){
            throw new BadRequestException();
        }

        site.setTitle(newSiteJson.get("title").asText());
        site.setDescription(newSiteJson.get("description").asText());
        site.setUri(new URI(newSiteJson.get("uri").asText()));

        return site;
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public @NotNull CustomSite delete(
            @NotNull @PathParam("id") UUID id
    ) throws URISyntaxException {
        log.info("Deleting a custom site: {}", id);
        Person user = this.getFullUser();

        CustomSite site = this.customSiteRepository.find("id", id).firstResultOptional()
                .orElseThrow(NotFoundException::new);

        if(!site.getPerson().equals(user)){
            throw new BadRequestException();
        }

        this.customSiteRepository.delete(site);
        user.getCustomSites().remove(site);
        this.getPersonRepository().persist(user);

        return site;
    }
}
