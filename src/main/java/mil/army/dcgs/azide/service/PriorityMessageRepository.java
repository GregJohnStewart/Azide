package mil.army.dcgs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import mil.army.dcgs.azide.config.PriorityMessageConfig;
import mil.army.dcgs.azide.entities.model.ClassificationBanner;
import mil.army.dcgs.azide.entities.model.PriorityMessage;

import java.util.List;

@Slf4j
@Named("PriorityMessageRepository")
@ApplicationScoped
public class PriorityMessageRepository implements PanacheRepository<PriorityMessage> {

    public void populate() {
        this.persist(
            PriorityMessage.builder()
            .title("Message 1")
            .priority("1")
            .date("12/30/2024")
            .content("Message 1 content")
            .build()
            );
    }

}