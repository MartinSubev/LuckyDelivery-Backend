package LuckyDelivery.Repository;

import LuckyDelivery.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);  // This already exists in JpaRepository, but it's good practice to declare it explicitly.
    Optional<User> findByIdAndType(Long id, String type);
}
