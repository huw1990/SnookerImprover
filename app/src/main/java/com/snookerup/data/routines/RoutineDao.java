package com.snookerup.data.routines;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.snookerup.model.Routine;

import java.util.List;

/**
 * Data access object for all information relating to routines.
 *
 * @author Huwdunnit
 */
@Dao
public interface RoutineDao {

    /**
     * Get all routines in the DB, ordered by the routine ID.
     * @return All routines in the DB, as a list
     */
    @Query("SELECT * FROM Routine ORDER BY id")
    LiveData<List<Routine>> getAllRoutines();

    /**
     * Get just the name of each routine.
     * @return A list of strings for the routine names in the DB
     */
    @Query("SELECT name FROM Routine ORDER BY id")
    List<String> getAllRoutineNames();

    /**
     * Get a practice routine from it's name.
     * @param routineName The name of the routine to get
     * @return The routine that matches the provided name
     */
    @Query("SELECT * FROM Routine WHERE name = :routineName")
    Routine getRoutineFromName(String routineName);

    /**
     * Add a number of routines the the DB.
     * @param routines Routines to add to the DB
     */
    @Insert
    void addRoutines(List<Routine> routines);
}
