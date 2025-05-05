package LuckyDelivery.Service;

import LuckyDelivery.Model.Cart;
import LuckyDelivery.Model.User;
import LuckyDelivery.Repository.CartRepository;
import LuckyDelivery.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                Optional<Cart> cartItemOptional = cartRepository.findById(cartItemId);
                if (cartItemOptional.isPresent()) {
                    Cart cartItem = cartItemOptional.get();
                    if (cartItem.getUser().getId().equals(user.getId())) { // Corrected line
                        System.out.println("Cart item with ID " + cartItemId + " belongs to user " + username + ". Deleting...");
                        cartRepository.deleteById(cartItemId);
                        return true;
                    } else {
                        System.out.println("Cart item with ID " + cartItemId + " does not belong to user " + username + ". Forbidden.");
                        return false;
                    }
                } else {
                    System.out.println("Cart item with ID " + cartItemId + " not found.");
                    return false;
                }
            } else {
                System.out.println("Could not retrieve user details for " + username);
                return false;
            }
        } else {
            System.out.println("No authenticated user found.");
            return false;
        }
    }
}