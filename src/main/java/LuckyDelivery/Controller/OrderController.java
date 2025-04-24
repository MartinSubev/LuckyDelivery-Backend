package LuckyDelivery.Controller;

import LuckyDelivery.Model.Order;
import LuckyDelivery.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Извлича всички поръчки
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Създаване на нова поръчка
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // Извличане на поръчки по потребител
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getMyOrders() {
        System.out.println("Executing getMyOrders()"); // Add this line
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Map) {
            Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
            System.out.println("Principal: " + principal); // Log the principal
            String userIdStr = (String) principal.get("userId");
            try {
                Long userId = Long.parseLong(userIdStr);
                System.out.println("Extracted userId from principal: " + userId); // Log the userId
                List<Order> orders = orderService.getOrdersByUser(userId);
                return ResponseEntity.ok(orders);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing userId: " + e.getMessage()); // Log parsing error
                return ResponseEntity.status(500).body(null); // Handle potential parsing error
            }
        } else {
            System.err.println("Authentication is null or principal is not a Map."); // Log authentication issue
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

    // Извличане на поръчка по ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}