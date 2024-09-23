package net.gjs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;
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

    @NotNull
    @OneToMany(mappedBy = "person")
    @Builder.Default
    private Set<CustomSite> customSites = new LinkedHashSet<>();

    @NotNull
    @OneToMany
    @Builder.Default
    private Set<ProvidedSite> favoriteProvidedSites = new LinkedHashSet<>();
}
