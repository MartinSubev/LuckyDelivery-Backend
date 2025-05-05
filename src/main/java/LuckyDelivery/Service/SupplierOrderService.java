package LuckyDelivery.Service;

import LuckyDelivery.Model.Enums.OrderStatus;
import LuckyDelivery.Model.Order;
import LuckyDelivery.Model.User;
import LuckyDelivery.Repository.OrderRepository;
import LuckyDelivery.Repository.UserRepository; // Make sure to inject this
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SupplierOrderService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierOrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    public List<Order> getCollectedOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Map) {
            Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
            if (principal.containsKey("userId")) {
                String userIdString = (String) principal.get("userId");
                try {
                    Long supplierId = Long.parseLong(userIdString);
                    logger.info("Fetching collected orders for supplier ID from JWT principal: {}", supplierId);
                    return orderRepository.findBySupplierIdAndStatus(supplierId, OrderStatus.IN_TRANSIT);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid userId format in JWT principal: {}", userIdString);
                    return Collections.emptyList();
                }
            } else {
                logger.warn("userId not found in JWT principal.");
                return Collections.emptyList();
            }
        } else {
            logger.warn("Authentication principal is not the expected Map type.");
            return Collections.emptyList();
        }
    }

    public boolean assignOrderToSupplier(Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Map) {
            Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
            if (principal.containsKey("userId")) {
                String userIdString = (String) principal.get("userId");
                try {
                    Long supplierId = Long.parseLong(userIdString);
                    logger.info("Attempting to assign order {} to supplier ID from JWT: {}", orderId, supplierId);
                    Optional<Order> orderOptional = orderRepository.findById(orderId);
                    if (orderOptional.isPresent()) {
                        Order order = orderOptional.get();
                        if (order.getStatus() == OrderStatus.PENDING && order.getSupplier() == null) {
                            Optional<User> supplierOptional = userRepository.findById((long) supplierId); // Fetch User entity
                            if (supplierOptional.isPresent()) {
                                order.setSupplier(supplierOptional.get()); // Set the User entity
                                order.setStatus(OrderStatus.IN_TRANSIT);
                                orderRepository.save(order);
                                return true;
                            } else {
                                logger.warn("Supplier with ID {} not found.", supplierId);
                                return false;
                            }
                        } else if (order.getSupplier() != null) {
                            logger.warn("Order {} is already assigned to supplier {}", orderId, order.getSupplier().getId());
                            return false;
                        } else {
                            logger.warn("Order {} is not in PENDING status.", orderId);
                            return false;
                        }
                    } else {
                        logger.warn("Order with ID {} not found.", orderId);
                        return false;
                    }
                } catch (NumberFormatException e) {
                    logger.warn("Invalid userId format in JWT principal for claiming order: {}", userIdString);
                    return false;
                }
            } else {
                logger.warn("userId not found in JWT principal for claiming order.");
                return false;
            }
        } else {
            logger.warn("Authentication principal is not the expected Map type for claiming order.");
            return false;
        }
    }

    public boolean markOrderAsDelivered(Long orderId) {
        logger.info("Attempting to mark order {} as delivered.", orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() == OrderStatus.IN_TRANSIT) {
                order.setStatus(OrderStatus.DELIVERED);
                orderRepository.save(order);
                return true;
            } else {
                logger.warn("Order {} is not in IN_TRANSIT status.", orderId);
                return false;
            }
        } else {
            logger.warn("Order with ID {} not found.", orderId);
            return false;
        }
    }

    public List<Order> getAvailableOrders() {
        logger.info("Fetching available orders (PENDING and no supplier).");
        return orderRepository.findByStatusAndSupplierIsNull(OrderStatus.PENDING); // Corrected to findBySupplierIsNull
    }

    public List<Order> getCompletedOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Map) {
            Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
            if (principal.containsKey("userId")) {
                String userIdString = (String) principal.get("userId");
                try {
                    Long supplierId = Long.parseLong(userIdString);
                    logger.info("Fetching completed orders for supplier ID from JWT principal: {}", supplierId);
                    return orderRepository.findBySupplierIdAndStatus(supplierId, OrderStatus.DELIVERED);
                } catch (NumberFormatException e) {
                    logger.warn("Invalid userId format in JWT principal for completed orders: {}", userIdString);
                    return Collections.emptyList();
                }
            } else {
                logger.warn("userId not found in JWT principal for completed orders.");
                return Collections.emptyList();
            }
        } else {
            logger.warn("Authentication principal is not the expected Map type for completed orders.");
            return Collections.emptyList();
        }
    }
}