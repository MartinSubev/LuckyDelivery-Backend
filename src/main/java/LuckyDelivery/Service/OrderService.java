package LuckyDelivery.service;

import LuckyDelivery.model.Order;
import LuckyDelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Извлича всички поръчки
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Създаване на нова поръчка
    public Order createOrder(Order order) {
        // Записваме поръчката
        return orderRepository.save(order);
    }

    // Извличане на поръчки по потребител
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Извличане на поръчка по ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
}
