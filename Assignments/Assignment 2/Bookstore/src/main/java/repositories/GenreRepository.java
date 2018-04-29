package repositories;

import model.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {

    Iterable<Genre> findAllByType(String type);
}
