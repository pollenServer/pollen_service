package pollen.pollen_service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pollen.pollen_service.utils.ApiUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import static pollen.pollen_service.utils.ApiUtils.error;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiUtils.ApiResult<?>> newResponse(String message, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(error(message, status), headers, status);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class,
            UnsupportedEncodingException.class
    })
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        return newResponse("잘못된 요청입니다.", HttpStatus.OK);
    }

    @ExceptionHandler({
            IOException.class,
            ParseException.class
    })
    public ResponseEntity<?> handleIOException(Exception e) {
        return newResponse("꽃가루농도지수를 불러올 수 없습니다.", HttpStatus.OK);
    }
}