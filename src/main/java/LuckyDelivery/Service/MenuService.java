package LuckyDelivery.Service;

import LuckyDelivery.Model.Product;  // Using your Product model
import LuckyDelivery.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductsByRestaurantId(Integer restaurantId) {
        return productRepository.findByRestaurantId(restaurantId);
    }
}
