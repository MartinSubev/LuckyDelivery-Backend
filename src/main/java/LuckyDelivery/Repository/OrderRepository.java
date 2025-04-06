package LuckyDelivery.repository;

import LuckyDelivery.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);  // Метод за намиране на поръчки по userId
}
