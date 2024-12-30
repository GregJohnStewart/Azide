package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import mil.army.dcgs.azide.config.PriorityMessageConfig;
import mil.army.dcgs.azide.entities.model.PriorityMessage;

@Slf4j
@Named("PriorityMessageRepository")
@ApplicationScoped
public class PriorityMessageRepository implements PanacheRepository<PriorityMessage> {

    @Inject
    PriorityMessageConfig priorityMessagesConfig;

    private boolean initted = false;

    public void populate() {
        if(this.initted){
            return;
        }

        log.info("Populating priority messages.");

        for (PriorityMessageConfig.Message curMessage : this.priorityMessagesConfig.messages()) {
            //Optional<ProvidedMessage> fromDb = this.find("title", curMessage.title()).firstResultOptional();

            //if (fromDb.isEmpty()) {
                log.debug("Adding new message: {}", curMessage.title());
                this.persist(
                        PriorityMessage.builder()
                                .title(curMessage.title())
                                .priority(curMessage.priority())
                                .date(curMessage.date())
                                .content(curMessage.content())
                                .build()
                );
           // }
        }
        this.initted = true;
    }

}