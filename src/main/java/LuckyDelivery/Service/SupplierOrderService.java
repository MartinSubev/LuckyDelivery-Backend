package LuckyDelivery.Service;

import LuckyDelivery.Model.Enums.OrderStatus;
import LuckyDelivery.Model.Order;
import LuckyDelivery.Model.User;
import LuckyDelivery.Repository.OrderRepository;
import LuckyDelivery.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;  // Add this to fetch users from the database

    // Get all orders with IN_TRANSIT status for a given supplier
    public List<Order> getOrdersByStatusAndSupplierId(Long supplierId, String status) {
        return orderRepository.findBySupplierIdAndStatus(supplierId, status);
    }

    // This method will allow a supplier to claim an order
    @Transactional
    public boolean assignOrderToSupplier(Long orderId, Long supplierId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            // Check if the order is not already in IN_TRANSIT or DELIVERED
            if (order.getStatus() == OrderStatus.PENDING) {
                // Fetch the supplier from the database by id
                Optional<User> supplierOptional = userRepository.findById(supplierId);
                if (supplierOptional.isPresent()) {
                    User supplier = supplierOptional.get();  // Fetch the supplier User entity from the database
                    // Assign the supplier to the order
                    order.setSupplier(supplier);  // No need to create a new User instance
                    order.setStatus(OrderStatus.IN_TRANSIT);
                    orderRepository.save(order);
                    return true;
                }
            }
        }
        return false;  // If order is already being delivered or completed, or user not found
    }

    // Method to mark an order as "DELIVERED"
    @Transactional
    public boolean markOrderAsDelivered(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() == OrderStatus.IN_TRANSIT) {
                order.setStatus(OrderStatus.DELIVERED);
                orderRepository.save(order);
                return true;
            }
        }
        return false; // If the order wasn't in transit
    }
}
