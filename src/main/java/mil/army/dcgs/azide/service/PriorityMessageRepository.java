package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import mil.army.dcgs.azide.entities.model.PriorityMessage;

@Slf4j
@Named("PriorityMessageRepository")
@ApplicationScoped
public class PriorityMessageRepository implements PanacheRepository<PriorityMessage> {

    public void populate() {
        this.persist(
            PriorityMessage.builder()
            .title("Message 1")
            .priority("1")
            .date("2024-12-30")
            .content("Message 1 content")
            .build()
            );
    }

}