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

        logger.debug("Fetching order with ID: {}", orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            logger.debug("Order found: ID={}, Status={}, Supplier={}", order.getId(), order.getStatus(), order.getSupplier());

            if (order.getStatus() == OrderStatus.PENDING) {
                logger.debug("Fetching supplier with ID: {} and type 'supplier'", supplierId);
                Optional<User> supplierOptional;
                try {
                    supplierOptional = userRepository.findByIdAndType(supplierId, User.UserType.supplier); // Changed argument
                    logger.debug("Supplier optional: {}", supplierOptional);
                } catch (Exception e) {
                    logger.error("Error fetching supplier: {}", e.getMessage(), e);
                    return false;
                }

                if (supplierOptional.isPresent()) {
                    User supplier = supplierOptional.get();
                    logger.debug("Supplier found: ID={}, Type={}", supplier.getId(), supplier.getType());

                    logger.debug("Setting supplier {} to order {}", supplier.getId(), order.getId());
                    order.setSupplier(supplier);
                    order.setStatus(OrderStatus.IN_TRANSIT);
                    logger.debug("Order status set to: {}", order.getStatus());

                    logger.debug("Saving order: {}", order);
                    try {
                        orderRepository.save(order);
                        logger.info("Order {} successfully assigned to supplier {}", orderId, supplierId);
                        return true;
                    } catch (Exception e) {
                        logger.error("Error saving order {} after assignment: {}", orderId, e.getMessage(), e);
                        return false;
                    }
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
                order.setStatus(OrderStatus.DELIVERED); // This call will go to the empty method
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