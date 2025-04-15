package LuckyDelivery.Service;

import LuckyDelivery.Model.Enums.OrderStatus;
import LuckyDelivery.Model.Order;
import LuckyDelivery.Model.User;
import LuckyDelivery.Repository.OrderRepository;
import LuckyDelivery.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierOrderService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierOrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all available orders (PENDING status and no supplier assigned)
    public List<Order> getAvailableOrders() {
        logger.info("Fetching available orders (PENDING and no supplier).");
        return orderRepository.findByStatusAndSupplierIsNull(OrderStatus.PENDING);
    }

    // This method allows a supplier to claim an order
    @Transactional
    public boolean assignOrderToSupplier(Long orderId, Long supplierId) {
        logger.info("Attempting to assign order {} to supplier {}", orderId, supplierId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Optional<User> supplierOptional = userRepository.findByIdAndType(supplierId, "supplier");

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() == OrderStatus.PENDING) {
                if (supplierOptional.isPresent()) {
                    User supplier = supplierOptional.get();
                    order.setSupplier(supplier);
                    order.setStatus(OrderStatus.IN_TRANSIT);
                    orderRepository.save(order);
                    logger.info("Order {} successfully assigned to supplier {}", orderId, supplierId);
                    return true;
                } else {
                    logger.warn("Supplier with ID {} and type 'supplier' not found.", supplierId);
                    return false;
                }
            } else {
                logger.warn("Order {} is not PENDING. Current status: {}", orderId, order.getStatus());
                return false;
            }
        } else {
            logger.warn("Order with ID {} not found.", orderId);
            return false;
        }
    }

    // Get all collected orders for a specific supplier (IN_TRANSIT status)
    public List<Order> getCollectedOrders(Long supplierId) {
        logger.info("Fetching collected orders for supplier {}", supplierId);
        return orderRepository.findBySupplierIdAndStatus(supplierId, OrderStatus.IN_TRANSIT);
    }

    // Method to mark an order as "DELIVERED"
    @Transactional
    public boolean markOrderAsDelivered(Long orderId) {
        logger.info("Attempting to mark order {} as delivered.", orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() == OrderStatus.IN_TRANSIT) {
                order.setStatus(OrderStatus.DELIVERED);
                orderRepository.save(order);
                logger.info("Order {} marked as delivered.", orderId);
                return true;
            } else {
                logger.warn("Order {} is not IN_TRANSIT. Current status: {}", orderId, order.getStatus());
                return false;
            }
        } else {
            logger.warn("Order with ID {} not found.", orderId);
            return false;
        }
    }

    // Get all completed orders for a specific supplier (DELIVERED status)
    public List<Order> getCompletedOrders(Long supplierId) {
        logger.info("Fetching completed orders for supplier {}", supplierId);
        return orderRepository.findBySupplierIdAndStatus(supplierId, OrderStatus.DELIVERED);
    }
}