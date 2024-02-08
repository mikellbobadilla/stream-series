package ar.app.models.serie;

import ar.app.models.genre.GenreModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "series")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SerieModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(columnDefinition = "TEXT NOT NULL")
    private String poster;
    @Column(nullable = false)
    private Integer seasons;
    @ManyToMany(mappedBy = "series", targetEntity = GenreModel.class)
    private Set<GenreModel> genres;
}
