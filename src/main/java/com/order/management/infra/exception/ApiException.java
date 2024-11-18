package com.order.management.infra.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@Getter
public class ApiException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus httpStatusCode;
    private ErrorApiDTO apiErrorResponse;

    public ApiException(HttpStatus httpStatusCode, String message, Exception e) {
        super(message, e);
        this.httpStatusCode = httpStatusCode;
        this.apiErrorResponse = ErrorApiDTO.builder()
                .error(message)
                .errorDescription(ApiException.getMessage(e))
                .build();
    }

    public ApiException(HttpStatus httpStatusCode, String message, String errorDescription) {
        this.httpStatusCode = httpStatusCode;
        this.apiErrorResponse = ErrorApiDTO.builder()
                .error(message)
                .errorDescription(errorDescription)
                .build();
    }

    public ApiException(HttpStatus httpStatus, String bankDataIdIsRequired) {
        this.httpStatusCode = httpStatus;
        this.apiErrorResponse = ErrorApiDTO.builder()
                .error(bankDataIdIsRequired)
                .build();

    }

    public static ApiException throwApiException(HttpStatus httpStatusCode, String message) {
        return new ApiException(httpStatusCode, message, new RuntimeException());
    }

    public static ApiException throwApiException(HttpStatus httpStatusCode, String message, String description) {
        return new ApiException(httpStatusCode, message, description);
    }

    public static ApiException throwApiException(HttpStatus httpStatusCode, String message, Exception e) {
        log.error("Exception: {}", e);
        return new ApiException(httpStatusCode, message, e);
    }

    private static String getMessage(Exception e) {
        if (e == null) {
            return null;
        }
        return e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
    }

    public ResponseEntity<ErrorApiDTO> toResponseEntity() {
        log.error(this.apiErrorResponse.toString());
        return ResponseEntity.status(this.httpStatusCode).body(this.apiErrorResponse);
    }
}
