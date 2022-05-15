package com.snookerup.data.scores;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.snookerup.data.AppDatabase;
import com.snookerup.model.RoutineScore;
import com.snookerup.model.RoutineScoreStats;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Repository for operations on scores.
 *
 * @author Huwdunnit
 */
public class ScoreRepository {

    private static final String TAG = ScoreRepository.class.getName();

    private ScoreDao scoreDao;

    public ScoreRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        scoreDao = db.scoreDao();
    }

    /**
     * Insert a score into the DB.
     * @param routineScore The score to add
     * @param successCallback A callback if the operation was a success
     * @param errorCallback A callback if the operation failed
     */
    public void insert(RoutineScore routineScore, Runnable successCallback, Runnable errorCallback) {
        Log.d(TAG, "insert, score=" + routineScore);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                scoreDao.insert(routineScore);
                Log.d(TAG, "Insert successful, notifying callback now");
                successCallback.run();
            } catch (Exception ex) {
                Log.e("Error inserting into DB, ex={}", ex.getMessage());
                ex.printStackTrace();
                errorCallback.run();
            }
        });
    }

    /**
     * Get stats for a particular routine. Performs a number of DB calls then returns the results
     * from these calls in a single object.
     * @param routineName The name of the routine
     * @param sinceDate The date to get stats from. If null, we search without any kind of
     *                  timeframe
     * @param successCallback A callback to be made when the operation is a success
     * @param errorCallback A callback to be made when the operation fails
     */
    public void getStatsForRoutine(String routineName, Date sinceDate,
                                   Consumer<RoutineScoreStats> successCallback, Runnable errorCallback) {
        Log.d(TAG, "getStatsForRoutine, routine name=" + routineName + " sinceDate=" + sinceDate);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "Making multiple DB calls in this one thread, to get attempts, best and average");
            try {
                int numAttempts;
                RoutineScore bestScore;
                double averageScore;
                if (sinceDate == null) {
                    Log.d(TAG, "Getting stats for all time");

                    numAttempts = scoreDao.getNumberOfAttemptsForRoutine(routineName);
                    Log.d(TAG, "numAttempts=" + numAttempts);

                    bestScore = scoreDao.getBestScoreForRoutine(routineName);
                    Log.d(TAG, "bestScore=" + bestScore);

                    averageScore = scoreDao.getAverageScoreForRoutine(routineName);
                    Log.d(TAG, "averageScore=" + averageScore);
                } else {
                    Log.d(TAG, "Getting stats since date=" + sinceDate);

                    numAttempts = scoreDao.getNumberOfAttemptsForRoutineSinceDate(routineName, sinceDate);
                    Log.d(TAG, "numAttempts=" + numAttempts);

                    bestScore = scoreDao.getBestScoreForRoutineSinceDate(routineName, sinceDate);
                    Log.d(TAG, "bestScore=" + bestScore);

                    averageScore = scoreDao.getAverageScoreForRoutineSinceDate(routineName, sinceDate);
                    Log.d(TAG, "averageScore=" + averageScore);
                }
                Log.d(TAG, "Building stats object");
                RoutineScoreStats routineStats = new RoutineScoreStats.RoutineScoreStatsBuilder()
                        .setNumberOfAttempts(numAttempts)
                        .setBestScore(bestScore)
                        .setAverageScore(averageScore)
                        .setRoutineName(routineName)
                        .setSinceDate(sinceDate)
                        .createRoutineScoreStats();
                Log.d(TAG, "Stats=" + routineStats);
                successCallback.accept(routineStats);
            } catch (Exception ex) {
                Log.e("Error getting stats for routine, ex={}", ex.getMessage());
                ex.printStackTrace();
                errorCallback.run();
            }
        });
    }

    /**
     * Get all scores for a particular routine, since the provided date (or all time, if a null
     * sinceDate is provided).
     * @param routineName The name of the routine
     * @param sinceDate The date from which we're interested. If null, we get all routines without
     *                  time constraints
     * @return A LiveData object encapsulating a List of RoutineScore objects
     */
    public LiveData<List<RoutineScore>> getAllScoresForRoutine(String routineName, Date sinceDate) {
        /* Returning LiveData means the data is loaded asynchronously, so we don't need to
        * execute this query in a thread from the thread pool. */
        if (sinceDate == null) {
            return scoreDao.loadAllForRoutine(routineName);
        } else {
            return scoreDao.loadAllForRoutineSinceData(routineName, sinceDate);
        }
    }

    /**
     * Delete a set of scores that have previously been entered.
     * @param scores The scores to delete
     * @param successCallback A callback if the operation is a success, providing the number of
     *                        deleted scores
     * @param errorCallback A callback if the operation fails
     */
    public void deleteMultipleScores(Set<RoutineScore> scores, Consumer<Integer> successCallback,
                                     Runnable errorCallback) {
        Log.d(TAG, "deleteMultipleScores, scores=" + scores);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "Deleting " + scores.size() + " scores");
            try {
                scoreDao.delete(scores.toArray(new RoutineScore[0]));
                Log.d(TAG, "Delete successful, notifying callback now");
                successCallback.accept(scores.size());
            } catch (Exception ex) {
                Log.e("Error deleting scores from the DB, ex={}", ex.getMessage());
                ex.printStackTrace();
                errorCallback.run();
            }
        });
    }

    /**
     * Check if we have any scores for a particular routine.
     * @param routineName The name of the routine
     * @param successCallback A callback if the operation was a success, providing a boolean to
     *                        indicate whether we have attempts for the routine
     * @param errorCallback A callback if the operation fails
     */
    public void haveAnyScoresForRoutine(String routineName, Consumer<Boolean> successCallback,
                                        Runnable errorCallback) {
        Log.d(TAG, "haveAnyScoresForRoutine, routineName=" + routineName);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "Getting best score for routine=" + routineName);
            try {
                RoutineScore bestScore = scoreDao.getBestScoreForRoutine(routineName);
                Log.d(TAG, "Best score=" + bestScore);
                successCallback.accept(bestScore != null);
            } catch (Exception ex) {
                Log.e("Error checking if we have any scores for a routine, ex={}", ex.getMessage());
                ex.printStackTrace();
                errorCallback.run();
            }
        });
    }
}