package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorDbService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorDbService directorDbService;

    @GetMapping
    public List<Director> getDirectors() {
        return directorDbService.getDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id) {
        return directorDbService.getDirectorsById();
    }

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        return directorDbService.addDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        return directorDbService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public int deleteDirectorById(@PathVariable Integer id) {
        return directorDbService.deleteDirectorById(id);
    }
}
