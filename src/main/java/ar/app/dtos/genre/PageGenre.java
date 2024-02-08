package ar.app.dtos.genre;

import lombok.Builder;

import java.util.List;

@Builder
public record PageGenre(
        List<GenreResponse> content,
        int pageNumber,
        int pageSize,
        int totalPages,
        long totalElements
) {
}
