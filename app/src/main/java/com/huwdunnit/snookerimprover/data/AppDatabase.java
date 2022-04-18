package com.huwdunnit.snookerimprover.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.huwdunnit.snookerimprover.model.Score;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages the internal SQLite DB, and the Room abstractions.
 *
 * @author Huwdunnit
 */
@Database(entities = {Score.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    private static final String NAME = "com.huwdunnit.snookerimprover-db";

    /** Number of threads available for DB operations. */
    private static final int NUMBER_OF_THREADS = 4;

    public abstract ScoreDao scoreDao();

    /**
     * All Room operations must be done off the main thread, so create a backing thread pool
     * with this specified number of threads.
     */
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Get the singleton DB instance.
     * @param context Context, used to build the DB
     * @return The DB
     */
    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}