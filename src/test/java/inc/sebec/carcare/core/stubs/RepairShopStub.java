package inc.sebec.carcare.core.stubs;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import inc.sebec.carcare.core.document.RepairShop;

@Component
public class RepairShopStub {

	public RepairShop getRandomRepairShop() {
		return RepairShop.builder()
						 .id(UUID.randomUUID().toString())
						 .name("myRandomRepairShop")
						 .description("This is description for random repair shop")
						 .admins(Set.of("MainAdmin"))
						 .mechanics(Set.of("MainMechanic"))
						 .owner("realOwnerId")
						 .build();
	}

	public RepairShop getRandomRepairShop(Integer number) {
		return RepairShop.builder()
						 .id(UUID.randomUUID().toString())
						 .name("myRandomRepairShop#" + number)
						 .description("This is description for random repair shop #" + number)
						 .admins(Set.of("MainAdmin"))
						 .mechanics(Set.of("MainMechanic"))
						 .owner("realOwnerId")
						 .build();
	}


	public List<RepairShop> getFewRandomRepairShops(Integer amount) {
		return IntStream.range(0, amount)
						.mapToObj(this::getRandomRepairShop)
						.collect(Collectors.toList());
	}
}
