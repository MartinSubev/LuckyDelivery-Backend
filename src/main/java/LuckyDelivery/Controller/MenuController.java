package LuckyDelivery.Controller;

import LuckyDelivery.Model.Product;  // Use your existing Product model
import LuckyDelivery.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu")
@CrossOrigin(origins = "*")
public class MenuController {

    @Autowired
    private MenuService menuService;

    // GET /api/restaurants/{restaurantId}/menu
    @GetMapping
    public ResponseEntity<List<Product>> menu(@PathVariable Integer restaurantId) {
        List<Product> products = menuService.getProductsByRestaurantId(restaurantId);
        return ResponseEntity.ok(products);
    }

    // Optional: POST to add an item to the cart (optional endpoint)
    @PostMapping("/items")
    public ResponseEntity<String> cartItem(@RequestBody Product product) {
        // Placeholder logic for adding to cart
        return ResponseEntity.ok("Product added to cart!");
    }
}

