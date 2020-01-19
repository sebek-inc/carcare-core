package inc.sebec.carcare.core.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import inc.sebec.carcare.core.document.RepairShop;
import inc.sebec.carcare.core.dto.response.RepairShopResponse;
import inc.sebec.carcare.core.exception.NotOwnerException;
import inc.sebec.carcare.core.repository.RepairShopRepository;
import inc.sebec.carcare.core.security.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RepairShopService {

	private final RepairShopRepository repository;
	private final ModelMapper mm;
	private final UserDetailsService userDetailsService;

	public Mono<RepairShopResponse> save(String id, RepairShop shop) {
		shop.setId(id);
		return repository.save(shop).map(this::toRepairShopResponse);
	}

	public Flux<RepairShopResponse> getAll() {
		return repository.findAll().map(this::toRepairShopResponse);
	}

	public Mono<RepairShopResponse> getById(String id) {
		return repository.findById(id).map(this::toRepairShopResponse);
	}

	public Mono<Void> deleteById(String id, String username) {
		if (isOwnerOfRepairShop(id, username)) {
			return repository.deleteById(id);
		} else {
			throw new NotOwnerException("Only owner can delete repair shop");
		}
	}

	public Boolean isOwnerOfRepairShop(String repairShopId, String username) {
		return repository.findById(repairShopId)
						 .zipWith(userDetailsService.findUserDetailByUsername(username))
						 .map(tuple -> tuple.getT1().getOwner().equals(tuple.getT2().getId()))
						 .blockOptional()
						 .orElse(false);
	}

	private RepairShopResponse toRepairShopResponse(RepairShop rs) {
		return mm.map(rs, RepairShopResponse.class);
	}
}
