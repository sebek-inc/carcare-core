package inc.sebec.carcare.core.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import inc.sebec.carcare.core.document.RepairShop;

@Repository
public interface RepairShopRepository extends ReactiveMongoRepository<RepairShop, String> {
}
