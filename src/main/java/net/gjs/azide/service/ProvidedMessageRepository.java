package net.gjs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import net.gjs.azide.config.ProvidedMessagesConfig;
import net.gjs.azide.entities.model.ProvidedMessage;

@Slf4j
@Named("ProvidedMessageRepository")
@ApplicationScoped
public class ProvidedMessageRepository implements PanacheRepository<ProvidedMessage> {

    @Inject
    ProvidedMessagesConfig providedMessagesConfig;

    private boolean initted = false;

    public void populate() {
        if(this.initted){
            return;
        }

        log.info("Populating provided messages.");

        for (ProvidedMessagesConfig.Message curMessage : this.providedMessagesConfig.messages()) {
            //Optional<ProvidedMessage> fromDb = this.find("title", curMessage.title()).firstResultOptional();

            //if (fromDb.isEmpty()) {
                log.debug("Adding new message: {}", curMessage.title());
                this.persist(
                        ProvidedMessage.builder()
                                .title(curMessage.title())
                                .content(curMessage.content())
                                .date(curMessage.date())
                                .priority(curMessage.priority())
                                .build()
                );
           // }
        }
        this.initted = true;
    }

}