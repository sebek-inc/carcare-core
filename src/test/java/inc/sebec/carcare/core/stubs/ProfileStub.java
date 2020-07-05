package inc.sebec.carcare.core.stubs;

import inc.sebec.carcare.core.document.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ProfileStub {

    @Autowired
    private UserDetailStub userDetailStub;

    public Profile getRandomProfile() {
        return Profile.builder()
                .id(UUID.randomUUID().toString())
                .firstName("John")
                .lastName("Doe")
                .fullName("John Doe")
                .vehicles(Set.of("BMW", "Audi"))
                .detail(userDetailStub.getRandomUserDetail())
                .build();
    }

    public Profile getRandomProfile(Integer number) {
        return Profile.builder()
                .id(UUID.randomUUID().toString())
                .firstName("John")
                .lastName("Doe#" + number)
                .fullName("John Doe#" + number)
                .vehicles(Set.of("BMW", "Audi"))
                .detail(userDetailStub.getRandomUserDetail())
                .build();
    }

    public List<Profile> getFewRandomProfiles(Integer amount) {
        return IntStream.range(0, amount)
                .mapToObj(this::getRandomProfile)
                .collect(Collectors.toList());
    }
}
