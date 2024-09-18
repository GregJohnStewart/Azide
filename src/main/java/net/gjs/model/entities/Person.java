package net.gjs.model.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
public class Person extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Basic(optional=false)
    public String name;

    @NotNull
    @OneToMany(mappedBy = "person")
    private Set<CustomSite> customSites = new LinkedHashSet<>();

    @NotNull
    @OneToMany
    private Set<ProvidedSite> favoriteProvidedSites = new LinkedHashSet<>();
}
