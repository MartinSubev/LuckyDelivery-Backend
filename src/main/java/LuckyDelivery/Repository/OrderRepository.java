package LuckyDelivery.Repository;

import LuckyDelivery.Model.Enums.OrderStatus;
import LuckyDelivery.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);  // Метод за намиране на поръчки по userId
    List<Order> findBySupplierId(Long supplierId);

    List<Order> findBySupplierIdAndStatus(Long supplierId, OrderStatus status); // Change String to OrderStatus
    List<Order> findByStatusAndSupplierIsNull(OrderStatus status);// You might also need a method if 'supplier' is a direct foreign key to the User table
    // List<Order> findBySupplier_IdAndStatus(Long supplierId, OrderStatus status);

}