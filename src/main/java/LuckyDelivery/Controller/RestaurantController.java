package LuckyDelivery.Controller;

import LuckyDelivery.Model.Restaurant;
import LuckyDelivery.Service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*") // Optional: Enable CORS for frontend requests
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    // GET /api/restaurants (List of all restaurants)
    @GetMapping
    public ResponseEntity<List<Restaurant>> restaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    // GET /api/restaurants/{id} (Restaurant by ID)
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> restaurant(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }
}
