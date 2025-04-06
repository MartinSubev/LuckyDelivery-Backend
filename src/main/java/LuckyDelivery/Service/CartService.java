package LuckyDelivery.Service;

import LuckyDelivery.Model.Cart;
import LuckyDelivery.Model.User;
import LuckyDelivery.Model.Product;
import LuckyDelivery.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getCartItemsByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    public Cart addToCart(Cart cartItem) {
        // Check if the same product already exists in the user's cart
        Optional<Cart> existingItem = cartRepository.findByUserIdAndProductId(
            cartItem.getUser().getId(), 
            cartItem.getProduct().getId()
        );

        if (existingItem.isPresent()) {
            // Update quantity if item exists
            Cart existing = existingItem.get();
            existing.setQuantity(existing.getQuantity() + cartItem.getQuantity());
            return cartRepository.save(existing);
        }

        // Save new item if it doesn't exist
        return cartRepository.save(cartItem);
    }

    public Cart updateCartItem(Integer cartItemId, Cart updatedCart) {
        Optional<Cart> existingCartItem = cartRepository.findById(cartItemId);

        if (existingCartItem.isPresent()) {
            Cart cartItem = existingCartItem.get();
            cartItem.setQuantity(updatedCart.getQuantity());
            return cartRepository.save(cartItem);
        } else {
            throw new NoSuchElementException("Cart item not found with id " + cartItemId);
        }
    }

    public void removeFromCart(Integer cartItemId) {
        if (cartRepository.existsById(cartItemId)) {
            cartRepository.deleteById(cartItemId);
        } else {
            throw new NoSuchElementException("Cart item not found with id " + cartItemId);
        }
    }

    public void clearUserCart(Integer userId) {
        List<Cart> userCartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(userCartItems);
    }
}