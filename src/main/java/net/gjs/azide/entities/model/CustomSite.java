package net.gjs.azide.entities.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomSite {
    @Id
    @GeneratedValue
    private UUID id;

//    @JoinColumn(name = "id")
    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @NotNull
    @Basic(optional=false)
    public String title;

    @NotNull
    @Basic(optional=false)
    public String description;

    @NotNull
    @Basic(optional=false)
    public URI uri;

    @Version
    LocalDateTime lastUpdated;
}
