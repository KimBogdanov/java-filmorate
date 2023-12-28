package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmDbService {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaStorage mpaStorage;
    private final DirectorStorage directorStorage;

    public Film addFilm(Film film) {
        Film newFilm = filmStorage.createFilm(film);
        if (film.getGenres() != null) {
            filmGenreStorage.updateGenres(film.getId(), film.getGenres());
        }
        if (!film.getDirectors().isEmpty()) {
            List<Director> uniqueDirectors = film.getDirectors().stream()
                    .distinct()
                    .collect(Collectors.toList());
            directorStorage.updateFilmDirectors(film.getId(), uniqueDirectors);
        }
        newFilm.setGenres(filmGenreStorage.getGenresByFilmId(newFilm.getId()));
        newFilm.setMpa(mpaStorage.getMpaById(newFilm.getMpa().getId()));
        newFilm.setDirectors(directorStorage.getFilmDirectorsById(film.getId()));
        film.setId(film.getId());
        return newFilm;
    }

    public Film updateFilm(Film film) {
        Film updateFilm = filmStorage.updateFilm(film);
        filmGenreStorage.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            filmGenreStorage.updateGenres(film.getId(), film.getGenres());
        }
        List<Director> uniqueDirectors = film.getDirectors()
                .stream()
                .distinct()
                .collect(Collectors.toList());

        List<Integer> uniqueDirectorsId = uniqueDirectors.stream()
                .map(director -> director.getId())
                .collect(Collectors.toList());

        List<Integer> oldDirectorsId = directorStorage.getFilmDirectorsById(film.getId())
                .stream()
                .map(director -> director.getId())
                .collect(Collectors.toList());

        if (!uniqueDirectorsId.equals(oldDirectorsId)) {
            directorStorage.deleteAllDirectorByFilmId(film.getId());
            if(!uniqueDirectors.isEmpty()){
            directorStorage.updateFilmDirectors(film.getId(), uniqueDirectors);
            }
        }
        updateFilm.setGenres(filmGenreStorage.getGenresByFilmId(film.getId()));
        updateFilm.setMpa(mpaStorage.getMpaById(updateFilm.getMpa().getId()));
        updateFilm.setDirectors(directorStorage.getFilmDirectorsById(film.getId()));
        return updateFilm;
    }

    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public void deleteFilms() {
        filmStorage.deleteAllFilms();
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(filmGenreStorage.getGenresByFilmId(film.getId()));
        film.setDirectors(directorStorage.getFilmDirectorsById(id));
        return film;
    }

    public int deleteFilmById(int id) {
        return filmStorage.deleteFilmById(id);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }
}
