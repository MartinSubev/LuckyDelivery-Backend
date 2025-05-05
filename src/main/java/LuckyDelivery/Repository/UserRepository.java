package LuckyDelivery.Repository;

import LuckyDelivery.Model.User;
import LuckyDelivery.Model.User.UserType; // Import the enum
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByIdAndType(Long id, UserType type); // Changed parameter type
    Optional<User> findByUsername(String username);

}