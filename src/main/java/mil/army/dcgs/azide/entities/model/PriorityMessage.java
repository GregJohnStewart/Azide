package mil.army.dcgs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityMessage extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Basic(optional=false)
    private String title;

    @NotNull
    @Basic(optional=false)
    private String priority;
    
    @NotNull
    @Basic(optional=false)
    private String date;

    @NotNull
    @Basic(optional=false)
    private String content;

    @Version
    LocalDateTime lastUpdated;
}
