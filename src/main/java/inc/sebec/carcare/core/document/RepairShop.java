package inc.sebec.carcare.core.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Document
@Data
@Builder
@With
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RepairShop {
	@Id
	private String id;
	private String name;
	private String description;
	private Set<String> mechanics;
	private Set<String> admins;
	@NotNull
	@Indexed
	private String owner;

}
