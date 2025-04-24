package LuckyDelivery.Service;

import LuckyDelivery.Model.Product; // Make sure this points to your Product model
import LuckyDelivery.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private ProductRepository productRepository;

    // Fetch products by restaurantId
    public List<Product> getProductsByRestaurantId(Integer restaurantId) {
        // Use the appropriate method to find products by the restaurant ID
        return productRepository.findByRestaurantId(restaurantId); // Ensure your repository has this method
    }

    // Create a new product for the menu
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(Integer id, Product updatedProduct) {
        System.out.println("Service received update request for productId: " + id);
        System.out.println("Updated product data in service: " + updatedProduct);
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setDescription(updatedProduct.getDescription()); // Make sure you are updating the description
        product.setRestaurant(updatedProduct.getRestaurant());
        return productRepository.save(product);
    }

    // Delete a product by ID
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }
}
