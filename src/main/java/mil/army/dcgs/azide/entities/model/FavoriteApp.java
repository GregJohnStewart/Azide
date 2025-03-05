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
public class FavoriteApp extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @ManyToOne()
    public ApplicationInfo app;

    @NotNull
    @Basic(optional=false)
    public int xLocation;

    @NotNull
    @Basic(optional=false)
    public int yLocation;

    @NotNull
    @Basic(optional=false)
    public int windowWidth;

    @NotNull
    @Basic(optional=false)
    public int windowHeight;
}
