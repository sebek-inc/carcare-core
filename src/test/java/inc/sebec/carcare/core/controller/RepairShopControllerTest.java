package inc.sebec.carcare.core.controller;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import inc.sebec.carcare.core.configuration.BaseTest;
import inc.sebec.carcare.core.document.RepairShop;
import inc.sebec.carcare.core.dto.response.RepairShopResponse;
import inc.sebec.carcare.core.repository.RepairShopRepository;
import inc.sebec.carcare.core.security.model.UserDetail;
import inc.sebec.carcare.core.security.service.UserDetailsService;
import inc.sebec.carcare.core.stubs.RepairShopStub;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RepairShopControllerTest extends BaseTest {
	@Autowired
	private WebTestClient client;

	@Autowired
	private ModelMapper mm;

	@Autowired
	private RepairShopStub repairShopStub;

	@MockBean
	private RepairShopRepository repository;

	@MockBean
	private UserDetailsService userDetailsService;

	@Test
	void should_return_401() {
		getAllRepairShops()
				.expectStatus().isUnauthorized();
	}

	@Test
	@WithMockUser
	void should_return_200_and_5_elements() {
		final int SIZE = 5;
		var repairShop = repairShopStub.getFewRandomRepairShops(SIZE);
		var expectedResult = mapRepairShopToDtos(repairShop);

		when(repository.findAll()).thenReturn(Flux.fromIterable(repairShop));

		getAllRepairShops()
				.expectStatus().isOk()
				.expectBodyList(RepairShopResponse.class).hasSize(SIZE).contains(expectedResult.get(0));
	}

	@Test
	@WithMockUser
	void should_save_repair_shop_and_return_201() {
		var repairShop = repairShopStub.getRandomRepairShop();
		var repairShopWithNullId = repairShop.withId(null);
		var repairShopWithGeneratedId = repairShop.withId("generatedId");
		var expectedResponse = mm.map(repairShopWithGeneratedId, RepairShopResponse.class);

		when(repository.save(any())).thenReturn(Mono.just(repairShopWithGeneratedId));

		createRepairShop(repairShop)
				.expectStatus().isCreated()
				.expectBody(RepairShopResponse.class).isEqualTo(expectedResponse);

		verify(repository).save(repairShopWithNullId);
	}

	@Test
	@WithMockUser
	void should_get_repair_shop_by_id() {
		var repairShop = repairShopStub.getRandomRepairShop();
		var expectedResponse = mm.map(repairShop, RepairShopResponse.class);

		when(repository.findById(repairShop.getId())).thenReturn(Mono.just(repairShop));

		getRepairShopWithId(repairShop.getId())
				.expectBody(RepairShopResponse.class).isEqualTo(expectedResponse);
	}

	@Test
	@WithMockUser
	void should_update_repair_shop_with_same_id_in_path_and_object() {
		var repairShop = repairShopStub.getRandomRepairShop();
		var expectedResponse = mm.map(repairShop, RepairShopResponse.class);

		when(repository.save(repairShop)).thenReturn(Mono.just(repairShop));

		updateRepairShopWithId(repairShop.getId(), repairShop)
				.expectStatus().isOk()
				.expectBody(RepairShopResponse.class).isEqualTo(expectedResponse);

		verify(repository).save(repairShop);
	}

	@Test
	@WithMockUser
	void should_update_repair_shop_with_different_id_in_path_and_object() {
		var repairShop = repairShopStub.getRandomRepairShop();
		var rsWithRealId = repairShop.withId("realId");
		var expectedResponse = mm.map(rsWithRealId, RepairShopResponse.class);

		when(repository.save(any())).thenReturn(Mono.just(rsWithRealId));

		updateRepairShopWithId("realId", repairShop)
				.expectStatus().isOk()
				.expectBody(RepairShopResponse.class).isEqualTo(expectedResponse);

		verify(repository).save(rsWithRealId);
	}

	@Test
	@WithMockUser(username = "realOwner")
	void should_delete_repair_shop_by_owner() {
		var repairShop = repairShopStub.getRandomRepairShop();
		var realOwner = new UserDetail("realOwnerId", "", "", Collections.emptySet());

		when(userDetailsService.findUserDetailByUsername("realOwner")).thenReturn(Mono.just(realOwner));
		when(repository.deleteById(repairShop.getId())).thenReturn(Mono.empty());
		when(repository.findById(repairShop.getId())).thenReturn(Mono.just(repairShop));

		deleteRepairShopWithId(repairShop.getId())
				.expectStatus()
				.isNoContent();

	}

	@Test
	@WithMockUser(username = "notOwner")
	void should_delete_repair_shop_by_not_owner() {
		var repairShop = repairShopStub.getRandomRepairShop();
		var realOwner = new UserDetail("notOwnerId", "", "", Collections.emptySet());

		when(userDetailsService.findUserDetailByUsername("notOwner")).thenReturn(Mono.just(realOwner));
		when(repository.deleteById(repairShop.getId())).thenReturn(Mono.empty());
		when(repository.findById(repairShop.getId())).thenReturn(Mono.just(repairShop));

		deleteRepairShopWithId(repairShop.getId())
				.expectStatus()
				.isForbidden();

	}

	private WebTestClient.ResponseSpec getAllRepairShops() {
		return client.get()
					 .uri(API.V1.REPAIR_SHOP)
					 .exchange();
	}

	private WebTestClient.ResponseSpec getRepairShopWithId(String id) {
		return client.get()
					 .uri(API.V1.REPAIR_SHOP + "/" + id)
					 .exchange();
	}

	private WebTestClient.ResponseSpec updateRepairShopWithId(String id, RepairShop repairShop) {
		return client.put()
					 .uri(API.V1.REPAIR_SHOP + "/" + id)
					 .contentType(MediaType.APPLICATION_JSON)
					 .body(BodyInserters.fromValue(repairShop))
					 .exchange();
	}

	private WebTestClient.ResponseSpec createRepairShop(RepairShop repairShop) {
		return client.post()
					 .uri(API.V1.REPAIR_SHOP)
					 .contentType(MediaType.APPLICATION_JSON)
					 .body(BodyInserters.fromValue(repairShop))
					 .exchange();
	}

	private WebTestClient.ResponseSpec deleteRepairShopWithId(String id) {
		return client.delete()
					 .uri(API.V1.REPAIR_SHOP + "/" + id)
					 .exchange();
	}

	private List<RepairShopResponse> mapRepairShopToDtos(Iterable<RepairShop> repairShop) {
		var dtosType = new TypeToken<List<RepairShopResponse>>() {}.getType();
		return mm.map(repairShop, dtosType);
	}
}
