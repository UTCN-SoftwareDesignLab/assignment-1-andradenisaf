package controllers;

import model.Book;
import model.Genre;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.IBookService;
import services.IGenreService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BookController {

    private IBookService bookService;
    private IGenreService genreService;

    @Autowired
    public BookController(IBookService bookService, IGenreService genreService) {
        this.bookService = bookService;
        this.genreService = genreService;
    }

    @RequestMapping(path = "/books", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String bookList(@RequestParam(value = "title", required = false) String title,
                           @RequestParam(value = "author", required = false) String author,
                           @RequestParam(value = "genre", required = false) String genre) {
        List<Book> books = new ArrayList<>();
        if (title != null) {
            books = bookService.findAllByTitle(title);
        } else if (author != null) {
            books = bookService.findAllByAuthor(author);
        } else if (genre != null) {
            Genre genre1 = genreService.findByName(genre);
            if (genre1 != null) {
                books = bookService.findAllByGenre(genre1);
            }
        } else {
            books = bookService.findAll();
        }

        JSONArray booksJSON = new JSONArray();
        for (Book book : books) {
            JSONObject bookJSON = book.toJSON();
            booksJSON.add(bookJSON);
        }

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("books", booksJSON);

        return responseJSON.toJSONString();
    }

    @RequestMapping(path = "/books", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBook(@RequestBody String bookJSON) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(bookJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Book book = new Book();

        String title = (String) jsonObject.get("title");
        book.setTitle(title);

        String author = (String) jsonObject.get("author");
        book.setAuthor(author);

        Long genreID = (Long) jsonObject.get("genre");
        Genre genre = genreService.findById(genreID);
        book.setGenre(genre);

        Integer quantity = (Integer) jsonObject.get("quantity");
        book.setQuantity(quantity);

        Double price = (Double) jsonObject.get("price");
        book.setPrice(price);

        book = bookService.create(book);

        return new ResponseEntity(book.toJSON().toJSONString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/books/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBook(@PathVariable("id") Long id, @RequestBody String bookJSON) {
        Book book = bookService.findById(id);
        if (book == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(bookJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String title = (String) jsonObject.get("title");
        if (title != null) {
            book.setTitle(title);
        }

        String author = (String) jsonObject.get("author");
        if (author != null) {
            book.setAuthor(author);
        }

        Long genreID = (Long) jsonObject.get("genre");
        if (genreID != null) {
            Genre genre = genreService.findById(genreID);
            if (genre != null) {
                book.setGenre(genre);
            }
        }

        Integer quantity = (Integer) jsonObject.get("quantity");
        if (quantity != null) {
            book.setQuantity(quantity);
        }

        Double price = (Double) jsonObject.get("price");
        if (price != null) {
            book.setPrice(price);
        }

        book = bookService.update(book);

        return new ResponseEntity(book.toJSON().toJSONString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/books/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteBook(@PathVariable("id") Long id) {
        Book book = bookService.findById(id);
        if (book == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        bookService.delete(book);
        return new ResponseEntity(HttpStatus.OK);
    }
}
