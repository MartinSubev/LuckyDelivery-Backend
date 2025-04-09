package LuckyDelivery.Service;

import LuckyDelivery.Model.Cart;
import LuckyDelivery.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    // Get all cart items for a user
    public List<Cart> getCartItemsByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    // Remove a product from the cart
    public boolean removeFromCart(Integer cartItemId) {
        if (cartRepository.existsById(cartItemId)) {
            cartRepository.deleteById(cartItemId);
            return true;
        } else {
            return false;  // Item not found
        }
    }
}
