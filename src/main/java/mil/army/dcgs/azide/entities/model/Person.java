package mil.army.dcgs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Basic(optional=false)
    @Column(unique=true)
    public String externalId;

    @NotNull
    @Basic(optional=false)
    public String name;
}
