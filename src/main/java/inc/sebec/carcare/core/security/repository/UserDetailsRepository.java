package inc.sebec.carcare.core.security.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import inc.sebec.carcare.core.security.model.UserDetail;
import reactor.core.publisher.Mono;

public interface UserDetailsRepository extends ReactiveMongoRepository<UserDetail, String> {
	Mono<UserDetail> findByUsername(String username);
}
