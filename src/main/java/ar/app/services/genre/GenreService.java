package ar.app.services.genre;

import ar.app.dtos.genre.GenreRequest;
import ar.app.dtos.genre.GenreResponse;
import ar.app.dtos.genre.PageGenre;
import ar.app.exceptions.genre.GenreException;
import ar.app.exceptions.genre.GenreNotFoundException;
import ar.app.models.genre.GenreModel;
import ar.app.repositories.genre.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreRepository repository;

    public PageGenre getGenres(int page, int size) {
        --page;

        if (page < 0) {
            throw new GenreException("Page cannot be less than 1");
        }

        Pageable pageable = PageRequest.of(page, size);
        var genres = repository.findAll(pageable);

        if (genres.isEmpty() && page > 0) {
            throw new GenreNotFoundException("No genres found.");
        }

        return pageMapper(genres);
    }

    public GenreResponse createGenre(GenreRequest request) {
        boolean exists = repository.existsByName(request.name());
        if (exists) throw new GenreException("Genre exists");

        GenreModel model = GenreModel.builder()
                .name(request.name())
                .build();
        return genreMapper(repository.saveAndFlush(model));
    }

   public GenreResponse getGenreBy(Long id) {
        GenreModel genre = repository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException("Genre not found"));
        return genreMapper(genre);
   }

   public void updateGenreBy(Long id, GenreRequest request) {
        if (repository.existsByNameAndNonId(request.name(), id))
            throw new GenreException("Genre exists");

        GenreModel genre = repository.findById(id)
                .orElseThrow(() -> new GenreNotFoundException("Genre not found"));
        genre.setName(request.name());
        repository.saveAndFlush(genre);
   }

   public void deleteGenreBy(Long id) {
        if (!repository.existsById(id))
            throw new GenreNotFoundException("Genre not found");
        repository.deleteById(id);
   }

    /* --------------------------------------------------------------------- */

    /* Private Methods */
    private GenreResponse genreMapper(GenreModel model) {
        return new GenreResponse(model.getId(), model.getName());
    }

    private PageGenre pageMapper(Page<GenreModel> models) {
        var contents = models.getContent();

        return PageGenre.builder()
                .content(contents.stream().map(this::genreMapper).toList())
                .pageNumber(models.getPageable().getPageNumber())
                .pageSize(models.getPageable().getPageSize())
                .totalPages(models.getTotalPages())
                .totalElements(models.getTotalElements())
                .build();
    }
}
