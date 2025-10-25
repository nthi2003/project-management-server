package com.skytech.projectmanagement.common.exception;

import java.util.HashMap;
import java.util.Map;
import com.skytech.projectmanagement.common.dto.ErrorDetails;
import com.skytech.projectmanagement.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex) {
        ErrorDetails errorDetails = new ErrorDetails("RESOURCE_NOT_FOUND", ex.getMessage(), null);
        ErrorResponse errorResponse = ErrorResponse.of("Không tìm thấy tài nguyên", errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorDetails errorDetails = new ErrorDetails("FORBIDDEN",
                "Bạn không có quyền thực hiện hành động này", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of("Truy cập bị từ chối", errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorDetails errorDetails =
                new ErrorDetails("VALIDATION_ERROR", "Dữ liệu không hợp lệ", errors);
        ErrorResponse errorResponse = ErrorResponse.of("Yêu cầu không hợp lệ", errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorDetails errorDetails = new ErrorDetails("INTERNAL_SERVER_ERROR",
                "Đã xảy ra lỗi hệ thống, vui lòng thử lại sau", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of("Lỗi máy chủ", errorDetails);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
