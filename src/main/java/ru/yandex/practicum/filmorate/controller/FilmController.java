package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен POST запрос на добавление фильма");
        validationFilm(film);
        film.setId(generateId);
        films.put(generateId++, film);
        log.debug("Добавлен новый фильм " + film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен PUT запрос на обновление фильма");
        validationFilm(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновлена информация о фильме " + film.getName() + ", с id = " + film.getId());
        } else if (film.getId() == null) {
            film.setId(generateId++);
            films.put(film.getId(), film);
            log.debug("Добавлен новый фильм " + film.getName());
        } else {
            log.debug("Не удалось обновить данные о фильме");
            throw new InvalidIdException("Неверный идентификатор фильма");
        }
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен GET запрос на получение фильмов");
        return new ArrayList<>(films.values());
    }

    private void validationFilm(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Название фильма не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Длина описания фильма не должна превышать 200 символов");
        }
        if (film.getDescription().isEmpty() || film.getDescription().isBlank()) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Описание фильма не должно быть пустым");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Дата релиза фильма не должна быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
