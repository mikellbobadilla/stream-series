package ar.app.advice;

import lombok.Builder;

@Builder
public record ErrorResponse(int status, String error) {
}
