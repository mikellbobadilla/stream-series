package ar.app.models.genre;

import ar.app.models.serie.SerieModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "genres")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GenreModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 10, nullable = false, unique = true)
    private String name;
    @ManyToMany
    private Set<SerieModel> series;
}
