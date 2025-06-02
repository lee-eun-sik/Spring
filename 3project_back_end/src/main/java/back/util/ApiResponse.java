package back.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
   
    private boolean success; // boolean 으로 변경
    private String message;
    private T data;

    public static ApiResponse success() {
        return new ApiResponse("SUCCESS", true, null);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse("ERROR", false, message);
    }

    public static ApiResponse of(String code, boolean success) {
        return new ApiResponse(code, success, null);
    }
}
