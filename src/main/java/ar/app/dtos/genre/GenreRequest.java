package ar.app.dtos.genre;

import jakarta.validation.constraints.NotBlank;

public record GenreRequest(@NotBlank String name) {
}
