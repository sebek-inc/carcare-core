package inc.sebec.carcare.core.security.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "user_details")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String username;

	@Size(min = 60, max = 60)
	private String password;

	private Set<Role> authorities = new HashSet<>();
}
