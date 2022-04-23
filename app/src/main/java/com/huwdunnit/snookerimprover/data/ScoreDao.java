package com.huwdunnit.snookerimprover.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.huwdunnit.snookerimprover.model.RoutineScore;

import java.util.Date;
import java.util.List;

/**
 * Data access object for all information relating to scores.
 *
 * @author Huwdunnit
 */
@Dao
public interface ScoreDao {

    /**
     * Get all scores for a particular routine.
     * @param name The name of the routine
     * @return A list of all scores for that routine
     */
    @Query("SELECT * FROM RoutineScore WHERE routineName = :name")
    List<RoutineScore> loadAllForRoutine(String name);

    /**
     * Get the number of times a routine has been attempted by the user.
     * @param name The name of the routine
     * @return The number of attempts, as an int
     */
    @Query("SELECT COUNT(*) FROM RoutineScore WHERE routineName = :name")
    int getNumberOfAttemptsForRoutine(String name);

    /**
     * Get the number of times a routine has been attempted since the provided date.
     * @param name The name of the routine
     * @param sinceDate The date we count from
     * @return The number of attempts, as an int
     */
    @Query("SELECT COUNT(*) FROM RoutineScore WHERE routineName = :name AND dateTime > :sinceDate")
    int getNumberOfAttemptsForRoutineSinceDate(String name, Date sinceDate);

    /**
     * Get the best score for a particular routine.
     * @param name The name of the routine to get the best score for
     * @return Details about the best score for a routine
     */
    @Query("SELECT * FROM RoutineScore WHERE routineName = :name ORDER BY score DESC LIMIT 1")
    RoutineScore getBestScoreForRoutine(String name);

    /**
     * Get the best score for a particular routine, since a particular date
     * @param name The name of the routine to get the best score for
     * @param sinceDate The date to search from
     * @return Details about the best score for a routine since the provided date
     */
    @Query("SELECT * FROM RoutineScore WHERE routineName = :name AND dateTime > :sinceDate ORDER BY score DESC LIMIT 1")
    RoutineScore getBestScoreForRoutineSinceDate(String name, Date sinceDate);

    /**
     * Get the average score for a particular routine.
     * @param name The name of the routine to get the average score for
     * @return The average score for the routine, as a double (to allow decimal values)
     */
    @Query("SELECT AVG(score) FROM RoutineScore WHERE routineName = :name")
    double getAverageScoreForRoutine(String name);

    /**
     * Get the average score for a particular routine, since a particular date.
     * @param name The name of the routine to get the average score for
     * @param sinceDate The date to search from
     * @return The average score for the routine since the provided date, as a double (to allow decimal values)
     */
    @Query("SELECT AVG(score) FROM RoutineScore WHERE routineName = :name AND dateTime > :sinceDate")
    double getAverageScoreForRoutineSinceDate(String name, Date sinceDate);

    /**
     * Insert a new score for a routine into the DB.
     * @param routineScore The score to add
     */
    @Insert
    void insert(RoutineScore routineScore);

    /**
     * Delete the provided score from the DB.
     * @param routineScore The routine to delete
     */
    @Delete
    void delete(RoutineScore routineScore);
}
