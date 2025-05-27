package back.controller.board;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import back.exception.HException;
import back.util.ApiResponse;

@ControllerAdvice// 컨트롤러에서 받는 에러를 다 잡아줌.
public class GlobalExceptionHandler {

    @ExceptionHandler(HException.class)
    public ResponseEntity<?> handleHException(HException ex) {
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ApiResponse<>(false, ex.getMessage(), null)); // 통신이 성공한게 아님. 데이터를 넣을 필요가 없다. 
    }
}

