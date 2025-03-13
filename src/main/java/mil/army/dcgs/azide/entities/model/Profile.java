package mil.army.dcgs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Basic(optional=false)
    public String username;

    @Builder.Default
    @NotNull
    @Basic(optional=false)
    public LocalDateTime lastSeen = LocalDateTime.now();

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<FavoriteApp> favorites = new ArrayList<>();
    
    /**
     * TODO:: is probably a better way to implement in the db side
     */
    @Transactional
    public boolean isFavoriteSet(String appRef) {
        for(FavoriteApp fav : favorites) {
            if(fav.getApp().getReference().equals(appRef)) {
                return true;
            }
        }
        return false;
    }
}
