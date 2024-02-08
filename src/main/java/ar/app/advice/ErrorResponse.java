package ar.app.advice;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse(int status, String error) {
}
