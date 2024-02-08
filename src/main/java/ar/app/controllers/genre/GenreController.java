package ar.app.controllers.genre;

import ar.app.dtos.genre.GenreRequest;
import ar.app.dtos.genre.GenreResponse;
import ar.app.dtos.genre.PageGenre;
import ar.app.services.genre.GenreService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genres")
@AllArgsConstructor
public class GenreController {

    private final GenreService service;

    @GetMapping
    ResponseEntity<PageGenre> genres(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(service.getGenres(page, size), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<GenreResponse> create(@RequestBody @Valid GenreRequest request) {
        return new ResponseEntity<>(service.createGenre(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<GenreResponse> getGenre(@PathVariable Long id) {
        return new ResponseEntity<>(service.getGenreBy(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateGenre(@PathVariable Long id, @RequestBody @Valid GenreRequest request) {
        service.updateGenreBy(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        service.deleteGenreBy(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
