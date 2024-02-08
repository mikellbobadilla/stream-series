package ar.app.advice.genre;

import ar.app.advice.ErrorResponse;
import ar.app.exceptions.genre.GenreException;
import ar.app.exceptions.genre.GenreNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenreAdvice {

    @ExceptionHandler(GenreException.class)
    ResponseEntity<ErrorResponse> genreException(GenreException exc) {
        return responseBuilder(HttpStatus.BAD_REQUEST, exc.getMessage());
    }

    @ExceptionHandler(GenreNotFoundException.class)
    ResponseEntity<ErrorResponse> genreNotFoundException(GenreNotFoundException exc) {
        return responseBuilder(HttpStatus.NOT_FOUND, exc.getMessage());
    }

    private ResponseEntity<ErrorResponse> responseBuilder(HttpStatus status, String message) {
        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())
                .error(message)
                .build();

        return new ResponseEntity<>(error, status);
    }
}
