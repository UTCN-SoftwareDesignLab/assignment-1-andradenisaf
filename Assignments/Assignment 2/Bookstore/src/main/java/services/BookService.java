package services;

import model.Book;
import model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookService implements IBookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        Iterable<Book> iterable = bookRepository.findAll();
        ArrayList<Book> books = new ArrayList<>();
        iterable.forEach(books::add);
        return books;
    }

    @Override
    public Book findById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book book;
        try {
            book = optionalBook.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return book;
    }

    @Override
    public List<Book> findAllByTitle(String title) {
        Iterable<Book> iterable = bookRepository.findAllByTitle(title);
        ArrayList<Book> books = new ArrayList<>();
        iterable.forEach(books::add);
        return books;
    }

    @Override
    public List<Book> findAllByAuthor(String author) {
        Iterable<Book> iterable = bookRepository.findAllByAuthor(author);
        ArrayList<Book> books = new ArrayList<>();
        iterable.forEach(books::add);
        return books;
    }

    @Override
    public List<Book> findAllByGenre(Genre genre) {
        Iterable<Book> iterable = bookRepository.findAllByGenre(genre);
        ArrayList<Book> books = new ArrayList<>();
        iterable.forEach(books::add);
        return books;
    }

    @Override
    public Book create(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            return null;
        }
        return bookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            return false;
        }
        bookRepository.delete(book);
        return true;
    }
}
