package inc.sebec.carcare.core.service.impl;

import inc.sebec.carcare.core.document.Profile;
import inc.sebec.carcare.core.dto.response.ProfileResponse;
import inc.sebec.carcare.core.exception.NotOwnerException;
import inc.sebec.carcare.core.repository.ProfileRepository;
import inc.sebec.carcare.core.service.ProfilesService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProfilesServiceV1 implements ProfilesService {

    private final ProfileRepository repository;
    private final ModelMapper modelMapper;


    @Override
    public Flux<ProfileResponse> getAll() {
        return repository.findAll().map(this::toProfileResponse);
    }

    @Override
    public Mono<ProfileResponse> save(String id, Profile profile) {
        profile.setId(id);
        return repository.save(profile).map(this::toProfileResponse);
    }

    @Override
    public Mono<ProfileResponse> getById(String id) {
        return repository.findById(id).map(this::toProfileResponse);
    }

    @Override
    public Mono<Void> deleteById(String id, String username) {
        boolean hasRightsToDelete = true; //TODO: find out who has rights
        if (hasRightsToDelete) {
            return repository.deleteById(id);
        } else {
            throw new NotOwnerException("Only owner can delete profile");
        }
    }

    private ProfileResponse toProfileResponse(Profile profile) {
        return modelMapper.map(profile, ProfileResponse.class);
    }

}
