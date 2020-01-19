package inc.sebec.carcare.core.document;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

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
