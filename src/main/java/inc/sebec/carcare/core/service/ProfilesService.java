package inc.sebec.carcare.core.service;

import inc.sebec.carcare.core.document.Profile;
import inc.sebec.carcare.core.dto.response.ProfileResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProfilesService {
    Flux<ProfileResponse> getAll();

    Mono<ProfileResponse> save(String id, Profile profile);

    Mono<ProfileResponse> getById(String id);

    Mono<Void> deleteById(String id, String username);
}
