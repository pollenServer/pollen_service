package pollen.pollen_service.utils;

import org.springframework.http.HttpStatus;

public class ApiUtils {

    public static <T> ApiResult<T> success(int status, T response) {
        return new ApiResult<>(status, response, null);
    }

    public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
        return new ApiResult<>(status.value(), null, new ApiError(throwable, status));
    }

    public static ApiResult<?> error(String message, HttpStatus status) {
        return new ApiResult<>(status.value(), null, new ApiError(message, status));
    }

    public static class ApiError {
        private final int status;
        private final String message;

        ApiError(Throwable throwable, HttpStatus status) {
            this(throwable.getMessage(), status);
        }

        ApiError(String message, HttpStatus status) {
            this.message = message;
            this.status = status.value();
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status;
        }
    }

    public static class ApiResult<T> {
        private final int status;
        private final T response;
        private final ApiError error;

        private ApiResult(int status, T response, ApiError error) {
            this.status = status;
            this.response = response;
            this.error = error;
        }

        public int isStatus() {
            return status;
        }

        public ApiError getError() {
            return error;
        }

        public T getResponse() {
            return response;
        }
    }
}