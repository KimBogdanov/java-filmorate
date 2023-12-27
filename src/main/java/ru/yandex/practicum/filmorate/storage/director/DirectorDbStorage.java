package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director createDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id");
        director.setId(simpleJdbcInsert.executeAndReturnKey(Map.of("director_name", director.getName())).intValue());
        return director;
    }

    @Override
    public Director getDirectorById(int id) {
        String sql = "SELECT * " +
                "FROM DIRECTORS " +
                "WHERE DIRECTOR_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::directorMapper, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Директор не найден");
        }
    }

    @Override
    public List<Director> getAllDirectors() {
        String sql = "SELECT * FROM DIRECTORS";
        return jdbcTemplate.query(sql, this::directorMapper);
    }

    @Override
    public Director updateDirector(Director director) {
        String sql = "UPDATE directors SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
        int result = jdbcTemplate.update(sql, director.getName(), director.getId());
        if (result != 1) {
            throw new NotFoundException("Директор не найден");
        }
        return director;
    }

    @Override
    public int deleteDirectorById(int id) {
        String sql = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        return jdbcTemplate.update(sql, id);
    }

    private Director directorMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getInt("director_id"),
                rs.getString("director_name"));
    }
}
