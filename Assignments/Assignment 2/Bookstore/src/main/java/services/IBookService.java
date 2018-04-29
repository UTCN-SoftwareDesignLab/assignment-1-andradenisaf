package services;

import model.Book;
import model.Genre;

import java.util.List;

public interface IBookService {

    List<Book> findAll();
    Book findById(Long id);
    List<Book> findAllByTitle(String title);
    List<Book> findAllByAuthor(String author);
    List<Book> findAllByGenre(Genre genre);
    Book create(Book book);
    Book update(Book book);
    boolean delete(Book book);
}
