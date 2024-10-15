package net.gjs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

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

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<CustomSite> customSites = new ArrayList<>();

    @NotNull
    @OneToMany
    @Builder.Default
    private Set<ProvidedSite> favoriteProvidedSites = new LinkedHashSet<>();
}
