package com.skytech.projectmanagement.common.exception;

import java.util.HashMap;
import java.util.Map;
import com.skytech.projectmanagement.common.dto.ErrorDetails;
import com.skytech.projectmanagement.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFound(MemberNotFoundException ex) {
        ErrorDetails details = new ErrorDetails("MEMBER_NOT_FOUND", // 404
                ex.getMessage(), null);
        return new ResponseEntity<>(ErrorResponse.of(ex.getMessage(), details),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundInRequestException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundInRequest(
            UserNotFoundInRequestException ex) {
        ErrorDetails details = new ErrorDetails("USER_NOT_FOUND", // 400
                ex.getMessage(), null);
        return new ResponseEntity<>(ErrorResponse.of("Dữ liệu không hợp lệ.", details),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleMemberAlreadyExists(
            MemberAlreadyExistsException ex) {
        ErrorDetails details = new ErrorDetails("MEMBER_ALREADY_EXISTS", // 409
                ex.getMessage(), null);
        return new ResponseEntity<>(ErrorResponse.of("Thành viên đã tồn tại.", details),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProjectKeyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProjectKeyExistsException(
            ProjectKeyExistsException ex) {

        ErrorDetails errorDetails = new ErrorDetails("PROJECT_KEY_EXISTS", ex.getMessage(), null);

        ErrorResponse errorResponse = ErrorResponse.of("Project key đã tồn tại.", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex) {
        ErrorDetails errorDetails =
                new ErrorDetails("FILE_VALIDATION_ERROR", ex.getMessage(), null);

        ErrorResponse errorResponse = ErrorResponse.of("Có lỗi với File Storage", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteConflictException.class)
    public ResponseEntity<ErrorResponse> handleDeleteConflictException(DeleteConflictException ex) {

        ErrorDetails errorDetails = new ErrorDetails("DELETE_CONFLICT", // 409
                ex.getMessage(), null);

        ErrorResponse errorResponse = ErrorResponse.of("Không thể xóa.", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExistsException(EmailExistsException ex) {

        ErrorDetails errorDetails = new ErrorDetails("EMAIL_EXISTS", // 409
                ex.getMessage(), null);

        ErrorResponse errorResponse = ErrorResponse.of("Email đã tồn tại.", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); // 409
    }

    @ExceptionHandler(InvalidOldPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOldPasswordException(
            InvalidOldPasswordException ex) {

        ErrorDetails errorDetails = new ErrorDetails("INVALID_OLD_PASSWORD", // 400
                ex.getMessage(), null);

        ErrorResponse errorResponse =
                ErrorResponse.of("Mật khẩu cũ không chính xác.", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(InvalidResetTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResetTokenException(
            InvalidResetTokenException ex) {

        ErrorDetails errorDetails = new ErrorDetails("INVALID_RESET_TOKEN", // 400
                ex.getMessage(), null);

        ErrorResponse errorResponse =
                ErrorResponse.of("Token không hợp lệ hoặc đã hết hạn.", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(RefreshTokenException ex) {

        ErrorDetails errorDetails = new ErrorDetails("INVALID_REFRESH_TOKEN", // 401
                ex.getMessage(), null);

        ErrorResponse errorResponse =
                ErrorResponse.of("Token không hợp lệ hoặc đã hết hạn.", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED); // 401
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {

        ErrorDetails errorDetails = new ErrorDetails("INVALID_TOKEN", // 400
                ex.getMessage(), null);

        ErrorResponse errorResponse = ErrorResponse.of("Token không hợp lệ.", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {

        ErrorDetails errorDetails = new ErrorDetails("INVALID_CREDENTIALS", // 401
                "Email hoặc mật khẩu không đúng.", ex.getMessage());

        ErrorResponse errorResponse =
                ErrorResponse.of("Email hoặc mật khẩu không đúng.", errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

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
