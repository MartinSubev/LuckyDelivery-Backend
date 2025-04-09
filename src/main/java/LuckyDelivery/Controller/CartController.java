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

    // GET: Retrieve all cart items for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Cart>> getUserCart(@PathVariable Integer userId) {
        List<Cart> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    // DELETE: Remove a specific product from the cart
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Integer cartItemId) {
        boolean isRemoved = cartService.removeFromCart(cartItemId);
        if (isRemoved) {
            return ResponseEntity.ok("Item removed from cart successfully");
        } else {
            return ResponseEntity.status(404).body("Item not found in cart");
        }
    }
}
