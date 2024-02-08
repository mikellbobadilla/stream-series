package ar.app.services.genre;

import ar.app.dtos.genre.GenreRequest;
import ar.app.dtos.genre.GenreResponse;
import ar.app.dtos.genre.PageGenre;
import ar.app.exceptions.genre.GenreException;
import ar.app.exceptions.genre.GenreNotFoundException;
import ar.app.models.genre.GenreModel;
import ar.app.repositories.genre.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {
    @Mock
    private GenreRepository repository;
    @InjectMocks
    private GenreService service;

    @Test
    void testGetGenres() {
        GenreModel model = genreBuilder();
        List<GenreModel> models = new ArrayList<>();
        models.add(model);
        Pageable pageable = PageRequest.of(0, 10);
        Page<GenreModel> genres = new PageImpl<>(models, pageable, 0);

        doReturn(genres).when(repository).findAll(pageable);

        PageGenre response = service.getGenres(1, 10);

        assertNotNull(response);
        assertFalse(response.content().isEmpty());
        verify(repository, atLeastOnce()).findAll(pageable);
    }

    @Test
    void testGetGenresEmpty() {
        List<GenreModel> models = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        Page<GenreModel> genres = new PageImpl<>(models, pageable, 0);

        doReturn(genres).when(repository).findAll(pageable);

        PageGenre response = service.getGenres(1, 10);
        assertNotNull(response);
        assertTrue(response.content().isEmpty());

        verify(repository, atLeastOnce()).findAll(pageable);
    }

    @Test
    void testGetGenrePageNoExists() {
        List<GenreModel> models = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 10);
        Page<GenreModel> genres = new PageImpl<>(models, pageable, 0);

        doReturn(genres).when(repository).findAll(pageable);

        assertThrows(GenreNotFoundException.class, () -> service.getGenres(2, 10));
        verify(repository, atLeastOnce()).findAll(pageable);
    }

    @Test
    void testGetGenrePageLessThanZero() {
        assertThrows(GenreException.class, () -> service.getGenres(0, 10));
    }

    @Test
    void testGetGenreById() {
        GenreModel model = genreBuilder();
        doReturn(Optional.of(model)).when(repository).findById(1L);

        GenreResponse response =  service.getGenreBy(1L);
        assertNotNull(response);
        assertEquals(model.getId(), response.id());

        verify(repository, atLeastOnce()).findById(1L);
    }

    @Test
    void testGetGenreByIdNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);

        assertThrows(GenreNotFoundException.class, () -> service.getGenreBy(1L));
        verify(repository, atLeastOnce()).findById(1L);
    }

    @Test
    void testUpdateGenre() {
        GenreModel model = genreBuilder();
        doReturn(Optional.of(model)).when(repository).findById(1L);

        GenreRequest request = new GenreRequest("ficción");

        assertDoesNotThrow(() -> service.updateGenreBy(1L, request));

        GenreResponse response = service.getGenreBy(1L);

        assertNotNull(response);
        assertEquals(request.name(), response.name());
        verify(repository, atLeastOnce()).findById(1L);
    }

    @Test
    void testUpdateGenreNotFound() {
        doReturn(Optional.empty()).when(repository).findById(1L);
        GenreRequest request = new GenreRequest("ficción");
        assertThrows(GenreNotFoundException.class, () -> service.updateGenreBy(1L, request));
        verify(repository, atLeastOnce()).findById(1L);
    }

    @Test
    void testDeleteGenre() {
        doReturn(true).when(repository).existsById(1L);

        assertDoesNotThrow(() -> service.deleteGenreBy(1L));
        verify(repository, atLeastOnce()).existsById(1L);
    }

    @Test
    void testDeleteGereNotFound() {
        doReturn(false).when(repository).existsById(1L);
        assertThrows(GenreNotFoundException.class, () -> service.deleteGenreBy(1L));
        verify(repository, atLeastOnce()).existsById(1L);
    }

    /* ----------------------------------------------------------------------- */
    private GenreModel genreBuilder() {
        return GenreModel.builder()
                .id(1L)
                .name("acción")
                .build();
    }
}