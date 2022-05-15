package com.snookerup.data.routines;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.snookerup.data.AppDatabase;
import com.snookerup.model.Routine;

import java.util.List;
import java.util.function.Consumer;

/**
 * Repository for operations on routines.
 *
 * @author Huwdunnit
 */
public class RoutineRepository {

    private static final String TAG = RoutineRepository.class.getName();

    private RoutineDao routineDao;

    public RoutineRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        routineDao = db.routineDao();
    }

    /**
     * Get a routine with the provided name.
     * @param routineName The name of the routine to get
     * @param successCallback A callback if the operation was a success, including the routine that
     *                        was obtained from the DB
     * @param errorCallback A callback if there was a problem with the operation, including any
     *                      exception that was thrown
     */
    public void getRoutineFromName(String routineName, Consumer<Routine> successCallback,
                                   Consumer<Exception> errorCallback) {
        Log.d(TAG, "getRoutineFromTitle, title=" + routineName);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Routine routine = routineDao.getRoutineFromName(routineName);
                Log.d(TAG, "Get routine successful, notifying callback now");
                successCallback.accept(routine);
            } catch (Exception ex) {
                Log.e("Error getting routine from title from the DB, ex={}", ex.getMessage());
                ex.printStackTrace();
                errorCallback.accept(ex);
            }
        });
    }

    /**
     * Get all routine names from the DB, as a list of strings
     * @param successCallback A callback if the operation was a success, including the routine names
     *                       that were obtained from the DB
     * @param errorCallback A callback if there was a problem with the operation, including any
     *                      exception that was thrown
     */
    public void getAllRoutineNames(Consumer<List<String>> successCallback,
                                   Consumer<Exception> errorCallback) {
        Log.d(TAG, "getAllRoutineNames");
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<String> titles = routineDao.getAllRoutineNames();
                Log.d(TAG, "Get routine names successful, notifying callback now");
                successCallback.accept(titles);
            } catch (Exception ex) {
                Log.e("Error getting routine names from the DB, ex={}", ex.getMessage());
                ex.printStackTrace();
                errorCallback.accept(ex);
            }
        });
    }

    /**
     * Get all routines from the DB, as a LiveData object (meaning this operation doesn't need to
     * be run in a separate thread, with callbacks to return the result).
     * @return A LiveData object containing a List of Routine objects.
     */
    public LiveData<List<Routine>> getAllRoutines() {
        return routineDao.getAllRoutines();
    }
}
