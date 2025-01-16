package mil.army.dcgs.azide.entities.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityMessage extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public UUID id;

    @NotNull
    @Basic(optional=false)
    public String title;

    @NotNull
    @Basic(optional=false)
    public String priority;
    
    @NotNull
    @Basic(optional=false)
    public String date;

    @NotNull
    @Basic(optional=false)
    public String content;

// Breaks the db call by being added to the call for ex: delete 
// Ex: Caused by: org.hibernate.exception.DataException: could not execute statement [No value specified for parameter 2.] [delete from PriorityMessage where id=? and lastUpdated=?]\r\n\tat 
// removing it 'fixes' the issue but not sure why
//    @Version
//    LocalDateTime lastUpdated;
}
