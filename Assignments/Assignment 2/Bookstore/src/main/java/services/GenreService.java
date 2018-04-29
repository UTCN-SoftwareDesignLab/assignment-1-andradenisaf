package services;

import model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GenreService implements IGenreService {

    private GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> findAll() {
        Iterable<Genre> iterable = genreRepository.findAll();
        ArrayList<Genre> genres = new ArrayList<>();
        iterable.forEach(genres::add);
        return genres;
    }

    @Override
    public Genre findById(Long id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        Genre genre;
        try {
            genre = optionalGenre.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return genre;
    }

    @Override
    public Genre findByName(String name) {
        Iterable<Genre> iterable = genreRepository.findAll();
        ArrayList<Genre> genres = new ArrayList<>();
        iterable.forEach(genres::add);
        return genres.get(0);
    }

    @Override
    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public boolean delete(Genre genre) {
        if (!genreRepository.existsById(genre.getId())){
            return false;
        }
        genreRepository.delete(genre);
        return true;
    }
}
