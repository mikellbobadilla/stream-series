package ar.app.repositories.genre;

import ar.app.models.genre.GenreModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreModel, Long> {

    boolean existsByName(String name);

    @Query("select count(*) > 0 from GenreModel g where g.name = :name AND g.id != :id")
    boolean existsByNameAndNonId(@Param("name") String name, @Param("id") Long id);
}
