package LuckyDelivery.Service;

import LuckyDelivery.Model.Order;
import LuckyDelivery.Model.User;
import LuckyDelivery.Repository.OrderRepository;
import LuckyDelivery.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // Извлича всички поръчки
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Създаване на нова поръчка
    public Order createOrder(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication object in createOrder: " + authentication);
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            System.out.println("Principal object in createOrder: " + principal);
            System.out.println("Principal class in createOrder: " + principal.getClass().getName());

            if (principal instanceof Map) {
                Map<?, ?> principalMap = (Map<?, ?>) principal;
                if (principalMap.containsKey("userDetails") && principalMap.get("userDetails") instanceof UserDetails) {
                    UserDetails userDetails = (UserDetails) principalMap.get("userDetails");
                    String username = userDetails.getUsername();

                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Потребител не е намерен: " + username));

                    order.setUser(user); // Associate the logged-in user with the order
                    return orderRepository.save(order);
                } else {
                    throw new IllegalStateException("UserDetails не е намерен в principal.");
                }
            } else {
                throw new IllegalStateException("Principal не е Map.");
            }
        } else {
            throw new IllegalStateException("Няма намерен автентикиран потребител.");
        }
    }

    // Извличане на поръчки по потребител
    public List<Order> getOrdersByUser(Long userId) {
        System.out.println("OrderService: Fetching orders for userId: " + userId);
        return orderRepository.findByUserId(userId);
    }

    // Извличане на поръчка по ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}