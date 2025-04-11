package LuckyDelivery.Repository;

import LuckyDelivery.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);  // Метод за намиране на поръчки по userId
    List<Order> findBySupplierId(Long supplierId);

    List<Order> findBySupplierIdAndStatus(Long supplierId, String status);
}
