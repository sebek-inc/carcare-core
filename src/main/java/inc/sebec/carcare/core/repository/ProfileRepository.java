package inc.sebec.carcare.core.repository;

import inc.sebec.carcare.core.document.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
