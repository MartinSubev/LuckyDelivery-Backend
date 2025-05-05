package LuckyDelivery.Controller;

import LuckyDelivery.Model.Cart;
import LuckyDelivery.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // Update with your frontend port
@RestController
@RequestMapping("/api/cart") // More standard API path
public class CartController {

    @Autowired
    private CartService cartService;

    // GET: Retrieve all cart items for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Cart>> getUserCart(@PathVariable Integer userId) {
        List<Cart> cartItems = cartService.getCartItemsByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    // POST: Add a product to the user's cart
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody Cart cartItem) {
        Cart addedItem = cartService.addItemToCart(cartItem);
        if (addedItem != null) {
            return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Or a more specific error
        }
    }

    // PUT: Update the quantity of an item in the cart
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<Cart> updateCartItemQuantity(@PathVariable Integer cartItemId, @RequestBody Cart updatedCartItem) {
        Cart updatedItem = cartService.updateItemQuantity(cartItemId, updatedCartItem.getQuantity());
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.status(404).body(null); // Item not found
        }
    }

    // DELETE: Remove a specific product from the cart
    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Integer cartItemId) {
        System.out.println("Received delete request for cartItemId: " + cartItemId);
        boolean isRemoved = cartService.removeFromCart(cartItemId);
        if (isRemoved) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}