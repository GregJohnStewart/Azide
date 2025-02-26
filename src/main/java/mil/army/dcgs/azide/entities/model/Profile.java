package mil.army.dcgs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @Basic(optional=false)
    public String username;

    @Builder.Default
    @NotNull
    @Basic(optional=false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:MM:SS")
    public LocalDateTime lastLogin = LocalDateTime.now();

    @NotNull
    @Basic(optional=false)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_id")
    public List<FavoriteApp> favorites;

    /**
     * Return the lastUpdated datetime as a formatted string
     */
    public String getFormattedLastLogin() {
        return dateTimeFormatter.format(lastLogin);
    }

    /**
     * Messy toJSON method...use a library?
     * @return JSON formatted profile
     */
    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to properly handle LocalDateTime serialization
        mapper.registerModule(new JavaTimeModule());

        // Disable serialization of dates as timestamps for a more human-readable format
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Profile to JSON", e);
        }
    }
}
