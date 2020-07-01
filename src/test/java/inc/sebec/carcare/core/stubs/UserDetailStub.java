package inc.sebec.carcare.core.stubs;

import inc.sebec.carcare.core.security.model.Role;
import inc.sebec.carcare.core.security.model.UserDetail;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class UserDetailStub {

    public UserDetail getRandomUserDetail() {
        UserDetail userDetail = new UserDetail();
        userDetail.setAuthorities(Set.of(new Role("ROLE_USER")));
        userDetail.setId(UUID.randomUUID().toString());
        userDetail.setUsername("username");
        userDetail.setPassword("password");
        return userDetail;
    }
}
