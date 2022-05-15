package com.snookerup.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.snookerup.data.routines.RoutineDao;
import com.snookerup.data.scores.ScoreDao;
import com.snookerup.model.Routine;
import com.snookerup.model.RoutineScore;
import com.snookerup.model.Routines;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages the internal SQLite DB, and the Room abstractions.
 *
 * @author Huwdunnit
 */
@Database(entities = {RoutineScore.class, Routine.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getName();

    private static volatile AppDatabase INSTANCE;

    private static final String NAME = "com.snookerup.app-db";

    /** Number of threads available for DB operations. */
    private static final int NUMBER_OF_THREADS = 4;

    public abstract ScoreDao scoreDao();

    public abstract RoutineDao routineDao();

    /**
     * All Room operations must be done off the main thread, so create a backing thread pool
     * with this specified number of threads.
     */
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Get the singleton DB instance.
     * @param context Context, used to build the DB
     * @return The DB
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, NAME)
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    databaseWriteExecutor.execute(() -> {
                                        Log.d(TAG, "Database created, adding starting routines");
                                        Routines routines = Routines.parseRoutinesFromAssets(context.getAssets());
                                        Log.d(TAG, "Routines to add=" + routines);
                                        getDatabase(context).routineDao().addRoutines(routines.getRoutines());
                                        Log.d(TAG, "Finished adding starting routines to DB");
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}