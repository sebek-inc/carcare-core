package inc.sebec.carcare.core.controller;

import inc.sebec.carcare.core.document.Profile;
import inc.sebec.carcare.core.dto.response.ProfileResponse;
import inc.sebec.carcare.core.service.ProfilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping(API.V1.PROFILES)
@RequiredArgsConstructor
public class ProfileController {

    private final ProfilesService profilesServiceV1;

    @GetMapping
    public Flux<ProfileResponse> getAll() {
        return profilesServiceV1.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProfileResponse> create(@RequestBody Profile profile) {
        return profilesServiceV1.save(null, profile);
    }

    @GetMapping("/{id}")
    public Mono<ProfileResponse> getById(@PathVariable String id) {
        return profilesServiceV1.getById(id);
    }

    @PutMapping("/id")
    public Mono<ProfileResponse> update(@PathVariable String id, @RequestBody Profile profile) {
        return profilesServiceV1.save(id, profile);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable String id, Principal principal) {
        return profilesServiceV1.deleteById(id, principal.getName());
    }
}
