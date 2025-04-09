package LuckyDelivery.Repository;

import LuckyDelivery.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(Integer userId);  // Retrieve cart items for a specific user
}
