package LuckyDelivery.Service;

import LuckyDelivery.Model.Cart;
import LuckyDelivery.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // Get all cart items for a user
    public List<Cart> getCartItemsByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    // Add a product to the cart
    public Cart addItemToCart(Cart cartItem) {
        // You might want to add logic here to check if the item already exists
        // in the user's cart and update the quantity instead of adding a duplicate.
        return cartRepository.save(cartItem);
    }

    // Update the quantity of an item in the cart
    public Cart updateItemQuantity(Integer cartItemId, Integer quantity) {
        Optional<Cart> cartOptional = cartRepository.findById(cartItemId);
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();
            cart.setQuantity(quantity);
            return cartRepository.save(cart);
        }
        return null; // Item not found
    }

    // Remove a product from the cart
    public boolean removeFromCart(Integer cartItemId) {
        System.out.println("CartService received request to delete cartItemId: " + cartItemId);
        if (cartRepository.existsById(cartItemId)) {
            System.out.println("Cart item with ID " + cartItemId + " found. Deleting...");
            cartRepository.deleteById(cartItemId);
            return true;
        } else {
            System.out.println("Cart item with ID " + cartItemId + " not found.");
            return false;
        }
    }
}