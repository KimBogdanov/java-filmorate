package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    Director createDirector(Director director);

    Director getDirectorById(int id);

    List<Director> getAllDirectors();

    Director updateDirector(Director director);

    int deleteDirectorById(int id);
}
