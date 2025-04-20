package LuckyDelivery.Model.DTO;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;  // Потребителско име
    private String email;     // Имейл
    private String password;  // Парола
    private String name;      // Име на потребителя
    private LuckyDelivery.Model.User.UserType type;    // Тип на потребителя (ENUM)
}