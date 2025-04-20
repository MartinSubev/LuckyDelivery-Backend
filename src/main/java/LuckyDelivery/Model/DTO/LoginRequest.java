package LuckyDelivery.Model.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;  // Потребителско име
    private String password;  // Парола
}