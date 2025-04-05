package LuckyDelivery.Controller;

import LuckyDelivery.Model.Cart;
import LuckyDelivery.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Get all cart items for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Cart>> getUserCart(@PathVariable Integer userId) {
        List<Cart> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    // Add item to cart
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody Cart cartItem) {
        Cart newCartItem = cartService.addToCart(cartItem);
        return ResponseEntity.ok(newCartItem);
    }

    // Update cart item quantity
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Integer cartItemId, @RequestBody Cart cartItem) {
        Cart updatedCartItem = cartService.updateCartItem(cartItemId, cartItem);
        return ResponseEntity.ok(updatedCartItem);
    }

    // Delete item from cart
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Integer cartItemId) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.ok("Item removed from cart successfully");
    }

    // Clear all items from user's cart
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Integer userId) {
        cartService.clearUserCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}