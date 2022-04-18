package com.huwdunnit.snookerimprover.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.huwdunnit.snookerimprover.model.Score;

import java.util.List;

/**
 * Data access object for all information relating to scores.
 *
 * @author Huwdunnit
 */
@Dao
public interface ScoreDao {

    @Query("SELECT * FROM score")
    List<Score> getAll();

    @Query("SELECT * FROM score WHERE routineName = :name")
    List<Score> loadAllForRoutine(String name);

    @Insert
    void insert(Score score);

    @Delete
    void delete(Score score);
}
