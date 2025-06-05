package back.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    public static ApiResponse success() {
        return new ApiResponse(true, "SUCCESS", null);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, "ERROR", message);
    }

    public static ApiResponse of(String message, boolean success) {
        return new ApiResponse(success, message, null);
    }
}