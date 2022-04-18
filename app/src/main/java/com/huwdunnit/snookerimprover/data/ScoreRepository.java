package com.huwdunnit.snookerimprover.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.huwdunnit.snookerimprover.model.Score;

import java.util.List;

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
     * @param score The score to add
     * @param successCallback A callback if the operation was a success
     * @param errorCallback A callback if the operation failed
     */
    public void insert(Score score, Runnable successCallback, Runnable errorCallback) {
        Log.d(TAG, "insert, score=" + score);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                scoreDao.insert(score);
                Log.d(TAG, "Insert successful, notifying callback now");
                successCallback.run();
            } catch (Exception ex) {
                Log.e("Error inserting into DB, ex={}", ex.getMessage());
                ex.printStackTrace();
                errorCallback.run();
            }
        });
    }
}