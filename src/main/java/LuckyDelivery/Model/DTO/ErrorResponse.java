package LuckyDelivery.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private Map<String, String> errors;
    private String path;

    public ErrorResponse(int status, String error, String path) {
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public ErrorResponse(int status, Map<String, String> errors, String path) {
        this.status = status;
        this.errors = errors;
        this.path = path;
    }
}