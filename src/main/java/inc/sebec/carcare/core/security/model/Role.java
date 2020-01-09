package inc.sebec.carcare.core.security.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document(collection = "authority")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Role implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(max = 50)
	@Id
	private String name;

	@Override
	public String getAuthority() {
		return name;
	}
}