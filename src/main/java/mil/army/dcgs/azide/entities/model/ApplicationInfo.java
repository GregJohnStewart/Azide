package mil.army.dcgs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    //TODO:: validate to "some-app" format (no whitespace, special chars)
    @NotNull
    @Basic(optional=false)
    @Column(unique=true)
    private String reference;
    
    @Builder.Default
    @Basic(optional=true)
    private String description = null;

    @NotNull
    @Basic(optional=false)
    private URI location;
    
    @NotNull
    @Basic(optional=false)
    private URI image;
    
    @NotNull
    @Basic(optional=false)
    private boolean showInAppBar;

    @Basic
    @Builder.Default
    private boolean defaultApp = false;

    @Basic
    @Builder.Default
    private boolean splashApp = false;

    @Basic
    @Builder.Default
    private URI splashEndpoint = null;

    @Version
    LocalDateTime lastUpdated;

    //TODO:: validate that splashEndpoint must be set if splashApp is true
    // TODO: Add small_icon, groups, description.
}
