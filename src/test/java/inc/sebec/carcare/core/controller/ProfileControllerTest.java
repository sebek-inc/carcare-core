package inc.sebec.carcare.core.controller;

import inc.sebec.carcare.core.document.Profile;
import inc.sebec.carcare.core.dto.response.ProfileResponse;
import inc.sebec.carcare.core.repository.ProfileRepository;
import inc.sebec.carcare.core.stubs.ProfileStub;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private ModelMapper mm;

    @Autowired
    private ProfileStub profileStub;

    @Autowired
    private ProfileRepository repository;

    @Test
    void shouldReturn401() {
        getAllProfiles()
                .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser
    void shouldReturn200Abd5Elements() {
        final int SIZE = 5;
        List<Profile> fewRandomProfiles = profileStub.getFewRandomProfiles(SIZE);
        List<ProfileResponse> expectedResult = mapProfilesToDtos(fewRandomProfiles);

        when(repository.findAll()).thenReturn(Flux.fromIterable(fewRandomProfiles));

        getAllProfiles()
                .expectStatus().isOk()
                .expectBodyList(ProfileResponse.class).hasSize(SIZE).contains(expectedResult.get(0));
    }

    @Test
    @WithMockUser
    void shouldSaveProfileAndReturn201() {
        Profile randomProfile = profileStub.getRandomProfile();
        Profile profileWithNullId = randomProfile.withId(null);
        Profile profileWithGeneratedId = randomProfile.withId("generatedId");
        ProfileResponse expectedResponse = mm.map(profileWithGeneratedId, ProfileResponse.class);

        when(repository.save(any())).thenReturn(Mono.just(profileWithGeneratedId));

        createProfile(randomProfile)
            .expectStatus().isCreated()
            .expectBody(ProfileResponse.class).isEqualTo(expectedResponse);

        verify(repository).save(profileWithNullId);
    }

    @Test
    @WithMockUser
    void shouldGetProfileById() {
        Profile randomProfile = profileStub.getRandomProfile();
        ProfileResponse expectedResult = mm.map(randomProfile, ProfileResponse.class);

        when(repository.findById(randomProfile.getId())).thenReturn(Mono.just(expectedResult));

        getProfileWithId(randomProfile.getId())
                .expectBody(ProfileResponse.class).isEqualTo(expectedResult);
    }

    @Test
    @WithMockUser
    void shouldUpdateProfileWithSameIdInPathAndObject() {
        Profile randomProfile = profileStub.getRandomProfile();
        ProfileResponse expectedResult = mm.map(randomProfile, ProfileResponse.class);

        when(repository.save(randomProfile)).thenReturn(Mono.just(randomProfile));

        updateProfileWithId(randomProfile.getId(), randomProfile)
                .expectStatus().isOk()
                .expectBody(ProfileResponse.class).isEqualTo(expectedResult);

        verify(repository).save(randomProfile);
    }

    @Test
    @WithMockUser
    void shouldUpdateProfileWithDifferentIfInPathAndObject() {
        Profile randomProfile = profileStub.getRandomProfile();
        Profile profileWithDifferentId = randomProfile.withId("differentId");
        ProfileResponse expectedResult = mm.map(profileWithDifferentId, ProfileResponse.class);

        when(repository.save(any())).thenReturn(Mono.just(profileWithDifferentId));

        updateProfileWithId("differentId", profileWithDifferentId)
                .expectStatus().isOk()
                .expectBody(ProfileResponse.class).isEqualTo(expectedResult);

        verify(repository).save(profileWithDifferentId);
    }

    private WebTestClient.ResponseSpec updateProfileWithId(String id, Profile profile) {
        return client.put()
                .uri(API.V1.PROFILES + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(profile))
                .exchange();
    }

    private WebTestClient.ResponseSpec getProfileWithId(String id) {
        return client.get()
                .uri(API.V1.PROFILES + "/" + id)
                .exchange();
    }

    private WebTestClient.ResponseSpec createProfile(Profile profile) {
        return client.post()
                .uri(API.V1.PROFILES)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(profile))
                .exchange();
    }

    private WebTestClient.ResponseSpec getAllProfiles() {
        return client.get()
                .uri(API.V1.REPAIR_SHOP)
                .exchange();
    }

    private List<ProfileResponse> mapProfilesToDtos(Iterable<Profile> profiles) {
        Type dtosType = new TypeToken<List<ProfileResponse>>() {}.getType();
        return mm.map(profiles, dtosType);
    }
}
