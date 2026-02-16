package ggolist.refactor.global.exception;

import ggolist.refactor.global.dto.response.ApiResponse;
import ggolist.refactor.global.exception.auth.InvalidTokenException;
import ggolist.refactor.global.exception.category.InvalidCategoryAccessException;
import ggolist.refactor.global.exception.favorite.FavoriteDuplicateException;
import ggolist.refactor.global.exception.favorite.FavoriteNotFoundException;
import ggolist.refactor.global.exception.mail.*;
import ggolist.refactor.global.exception.place.PlaceNotFoundException;
import ggolist.refactor.global.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검증 실패 (DTO Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException e) {
        // 모든 필드 에러 메시지를 문자열로 합치기
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", ")); // 쉼표로 구분

        return buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(UserNameNotFoundException.class)
    protected ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNameNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(EmailSendException.class)
    protected ResponseEntity<ApiResponse<?>> handleEmailSendException(EmailSendException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    protected ResponseEntity<ApiResponse<?>> handleEmailNotVerifiedException(EmailNotVerifiedException e) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(EmailDuplicateException.class)
    protected ResponseEntity<ApiResponse<?>> handleEmailDuplicateException(EmailDuplicateException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(VerificationCodeMismatchException.class)
    protected ResponseEntity<ApiResponse<?>> handleVerificationCodeMismatchException(VerificationCodeMismatchException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(VerificationCodeNotFoundException.class)
    protected ResponseEntity<ApiResponse<?>> handleVerificationCodeNotFoundException(VerificationCodeNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    protected ResponseEntity<ApiResponse<?>> handleInvalidPasswordException(InvalidPasswordException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    protected ResponseEntity<ApiResponse<?>> handleInvalidTokenException(InvalidTokenException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InvalidCategorySizeException.class)
    protected ResponseEntity<ApiResponse<?>> handleInvalidCategorySizeException(InvalidCategorySizeException e) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(InvalidCategoryAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCategory(InvalidCategoryAccessException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, 403, e.getMessage(), null));
    }

    @ExceptionHandler(PlaceNotFoundException.class)
    protected ResponseEntity<ApiResponse<?>> handlePlaceNotFoundException(PlaceNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(FavoriteDuplicateException.class)
    protected ResponseEntity<ApiResponse<?>> handleFavoriteDuplicateException(FavoriteDuplicateException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(FavoriteNotFoundException.class)
    protected ResponseEntity<ApiResponse<?>> handleFavoriteNotFoundException(FavoriteNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다: " + e.getMessage());
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponse(HttpStatus status, String message) {
        ApiResponse<?> response = new ApiResponse<>(false, status.value(), message);
        return ResponseEntity.status(status).body(response);
    }

    private <T> ResponseEntity<ApiResponse<T>> buildErrorResponse(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(false, status.value(), message, data);
        return ResponseEntity.status(status).body(response);
    }
}


