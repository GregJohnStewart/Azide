package mil.army.dcgs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;

@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationInfo extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Basic(optional=false)
    private String name;
    
    @Basic(optional=true)
    private String description;

    @NotNull
    @Basic(optional=false)
    private String location;
    
    @NotNull
    @Basic(optional=false)
    private String image;
    
    @NotNull
    @Basic(optional=false)
    private boolean showInAppBar;

    @Version
    LocalDateTime lastUpdated;
    
    // TODO: Add small_icon, groups.
}
