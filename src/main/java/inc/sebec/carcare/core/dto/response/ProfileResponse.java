package inc.sebec.carcare.core.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProfileResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String fullName;
}
