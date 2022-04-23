package com.huwdunnit.snookerimprover.data;

import android.content.Context;
import android.util.Log;

import com.huwdunnit.snookerimprover.model.RoutineScore;
import com.huwdunnit.snookerimprover.model.RoutineScoreStats;

import java.util.Date;
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
                if (sinceDate != null) {
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
}