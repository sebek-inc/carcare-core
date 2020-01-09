package inc.sebec.carcare.core.changelog;

import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;

import inc.sebec.carcare.core.security.model.Role;
import inc.sebec.carcare.core.security.model.UserDetail;
import lombok.SneakyThrows;

@ChangeLog
public class MongoChangeLog {

	@SneakyThrows
	@ChangeSet(id = "addSuperUser", order = "001", author = "rdiachuk")
	public void initial(MongoTemplate db) {
		var password = "$2a$10$F9gyUFB/89yYRGB4iL.W6epQawcdpM0r9Sm.btqxwWagWVooSX0TG";//123321
		db.save(new UserDetail("superuser",
							   "superuser",
							   password, Set.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER"))
		));
	}

}
