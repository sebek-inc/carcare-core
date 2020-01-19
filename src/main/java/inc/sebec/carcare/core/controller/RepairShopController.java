package inc.sebec.carcare.core.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import inc.sebec.carcare.core.document.RepairShop;
import inc.sebec.carcare.core.dto.response.RepairShopResponse;
import inc.sebec.carcare.core.service.RepairShopService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(API.V1.REPAIR_SHOP)
@RequiredArgsConstructor
public class RepairShopController {

	private final RepairShopService repairShopService;

	@GetMapping
	public Flux<RepairShopResponse> getAll() {
		return repairShopService.getAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<RepairShopResponse> create(@RequestBody RepairShop repairShop) {
		return repairShopService.save(null, repairShop);
	}

	@GetMapping("/{id}")
	public Mono<RepairShopResponse> getById(@PathVariable String id) {
		return repairShopService.getById(id);
	}

	@PutMapping("/{id}")
	public Mono<RepairShopResponse> update(@PathVariable String id, @RequestBody RepairShop repairShop) {
		return repairShopService.save(id, repairShop);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> deleteById(@PathVariable String id, Principal principal) {
		return repairShopService.deleteById(id, principal.getName());
	}

}
