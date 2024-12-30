package mil.army.dcgs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProvidedMessage extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Basic(optional=false)
    private String title;

    @NotNull
    @Basic(optional=false)
    private String content;

    @NotNull
    @Basic(optional=false)
    @Column(unique=true)
    private int priority;
    
    @NotNull
    @Basic(optional=false)
    private String date;

    //TODO:: image

    @Version
    LocalDateTime lastUpdated;
}
