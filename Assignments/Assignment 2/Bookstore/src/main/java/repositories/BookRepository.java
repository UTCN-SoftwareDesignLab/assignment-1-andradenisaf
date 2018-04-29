package repositories;

import model.Book;
import model.Genre;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

    Iterable<Book> findAllByTitle(String title);
    Iterable<Book> findAllByAuthor(String author);
    Iterable<Book> findAllByGenre(Genre genre);

}
