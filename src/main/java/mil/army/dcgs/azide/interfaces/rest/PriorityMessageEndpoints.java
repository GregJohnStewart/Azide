package mil.army.dcgs.azide.interfaces.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import mil.army.dcgs.azide.entities.model.PriorityMessage;
import mil.army.dcgs.azide.service.PriorityMessageRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequestScoped
@Path(RestInterface.BASE_PATH + "/app/messages")
public class PriorityMessageEndpoints extends RestInterface {

    @Inject
    PriorityMessageRepository priorityMessageRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public @NotNull List<PriorityMessage> getAllMessages() {
        List<PriorityMessage> output = priorityMessageRepository.listAll();

        return output;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public @NotNull PriorityMessage createMessage(ObjectNode priorityMessageJson) {
        PriorityMessage newMessage;

        newMessage = PriorityMessage.builder()
            .title(priorityMessageJson.get("title").asText())
            .priority(priorityMessageJson.get("priority").asText())
            .date(priorityMessageJson.get("date").asText())
            .content(priorityMessageJson.get("content").asText())
            .build();

        log.debug("New priority message (pre persist): {}", newMessage);

        this.getPriorityMessageRepository().persist(newMessage);

        log.debug("Created new priority message!");
        return newMessage;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public @NotNull PriorityMessage updateMessage(
        @NotNull @PathParam("id") UUID id,
        ObjectNode newPriorityMessageJson) {

        log.info("Updating a priority message: {}", newPriorityMessageJson);

        PriorityMessage message = this.priorityMessageRepository.find("id", id).firstResultOptional()
            .orElseThrow(NotFoundException::new);

        message.setTitle(newPriorityMessageJson.get("title").asText());
        message.setPriority(newPriorityMessageJson.get("priotiry").asText());
        message.setDate(newPriorityMessageJson.get("date").asText());
        message.setContent(newPriorityMessageJson.get("content").asText());

        return message;
    }

    @DELETE
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public @NotNull PriorityMessage deleteMessage(@NotNull @PathParam("id") UUID id) {
        log.info("Deleting a priority message: {}", id);

        PriorityMessage message = this.priorityMessageRepository.find("id", id).firstResultOptional()
            .orElseThrow(NotFoundException::new);

        this.priorityMessageRepository.delete(message);

        return message;
    }
}
