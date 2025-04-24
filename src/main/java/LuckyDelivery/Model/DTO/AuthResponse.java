package LuckyDelivery.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Added to support constructor injection
public class AuthResponse {

    private String token;
}