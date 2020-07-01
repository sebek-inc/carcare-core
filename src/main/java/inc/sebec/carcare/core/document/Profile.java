package inc.sebec.carcare.core.document;

import inc.sebec.carcare.core.security.model.UserDetail;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Profile {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
    private Set<String> vehicles;
    private UserDetail detail;
}
