package net.gjs.model.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
public class CustomSite extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

//    @JoinColumn(name = "id")
    @ManyToOne
    @JoinColumn(name = "person_id")
    @NotNull
    private Person person;

    @NotNull
    @Basic(optional=false)
    public String title;

    @NotNull
    @Basic(optional=false)
    public String description;

    @NotNull
    @Basic(optional=false)
    public URL url;

    @Version
    LocalDateTime lastUpdated;
}
