package ar.app.repositories.genre;

import ar.app.models.genre.GenreModel;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@Transactional(Transactional.TxType.NOT_SUPPORTED)
@ActiveProfiles("test")
class GenreRepositoryTest {

    @Autowired
    private TestEntityManager manager;

    @Test
    void contextLoads() {
        assertNotNull(manager);
    }

    @Test
    void verifyPersistGenreModel() {
        GenreModel model = genreBuilder();
        assertNull(model.getId());
        manager.persist(model);
        assertNotNull(model.getId());
    }

    @Test
    void verifyFindGenreModel() {
        GenreModel model = genreBuilder();
        manager.persist(model);
        assertNotNull(model.getId());
        GenreModel genre = manager.find(GenreModel.class, model.getId());
        assertNotNull(genre);
        assertEquals(model.getName(), genre.getName());
    }

    @Test
    void verifyUpdateGenreModel() {
        GenreModel model = genreBuilder();
        model = manager.persist(model);
        assertNotNull(model.getId());
        GenreModel genre = manager.find(GenreModel.class, model.getId());
        assertNotNull(genre);
        genre.setName("ficción");
        genre = manager.persist(genre);
        assertEquals(model.getId(), genre.getId());
        assertEquals(model.getName(), genre.getName());
    }

    @Test
    void verifyDeleteGenreModel() {
        GenreModel model = genreBuilder();
        model = manager.persist(model);
        assertNotNull(model.getId());
        GenreModel genre = manager.find(GenreModel.class, model.getId());
        assertNotNull(genre);
        manager.remove(genre);
        genre = manager.find(GenreModel.class, model.getId());
        assertNull(genre);
    }

    private GenreModel genreBuilder() {
        return GenreModel.builder()
                .name("acción")
                .build();
    }
}