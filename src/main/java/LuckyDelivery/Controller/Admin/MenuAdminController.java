package LuckyDelivery.Controller.Admin;

import LuckyDelivery.Model.Product;
import LuckyDelivery.Model.Restaurant;
import LuckyDelivery.Service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/restaurants/{restaurantId}/menu")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('EMPLOYEE')")
public class MenuAdminController {

    @Autowired
    private MenuService menuService;

    // GET: Retrieve all products for a specific restaurant (Admin)
    @GetMapping
    public ResponseEntity<List<Product>> getMenuItems(@PathVariable Integer restaurantId) {
        List<Product> products = menuService.getProductsByRestaurantId(restaurantId);
        return ResponseEntity.ok(products);
    }

    // POST: Create a new product for the restaurant menu (Admin)
    @PostMapping
    public ResponseEntity<Product> createProduct(@PathVariable Integer restaurantId, @RequestBody Product product) {
        product.setRestaurant(new Restaurant());
        product.getRestaurant().setId(restaurantId);
        Product createdProduct = menuService.createProduct(product);
        return ResponseEntity.status(201).body(createdProduct);
    }

    // PUT: Update a specific product for a restaurant (Admin)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer restaurantId, @PathVariable Integer id, @RequestBody Product updatedProduct) {
        System.out.println("Received update request for restaurantId: " + restaurantId + ", productId: " + id);
        System.out.println("Updated product data: " + updatedProduct);
        updatedProduct.setRestaurant(new Restaurant());
        updatedProduct.getRestaurant().setId(restaurantId);
        Product product = menuService.updateProduct(id, updatedProduct);
        return ResponseEntity.ok(product);
    }

    // DELETE: Delete a product from the menu (Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer restaurantId, @PathVariable Integer id) {
        // You might want to verify if the product belongs to this restaurant in the service layer
        menuService.deleteProduct(id); // Consider updating the service method if needed
        return ResponseEntity.ok("Product deleted successfully");
    }
}