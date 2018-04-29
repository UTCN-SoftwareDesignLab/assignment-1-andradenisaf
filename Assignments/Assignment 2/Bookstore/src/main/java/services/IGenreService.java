package services;

import model.Genre;

import java.util.List;

public interface IGenreService {

    List<Genre> findAll();
    Genre findById(Long id);
    Genre findByName(String name);
    Genre create(Genre genre);
    boolean delete(Genre genre);
}
