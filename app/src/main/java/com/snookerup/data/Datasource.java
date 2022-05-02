package com.snookerup.data;

import com.snookerup.R;
import com.snookerup.model.Routine;

import java.util.ArrayList;
import java.util.List;

/**
 * Datasource for obtaining practice routines from the app's resources.
 *
 * @author Huwdunnit
 */
public class Datasource {

    private Datasource() {}

    private static List<Routine> ROUTINES;

    /**
     * Load all hardcoded practice routines from app resources.
     * @return All practice routines
     */
    public static List<Routine> getRoutines() {
        if (ROUTINES == null) {
            ROUTINES = new ArrayList<>();
            ROUTINES.add(new Routine.RoutineBuilder()
                    .setStringResourceId(R.string.routine_1)
                    .setImageResourceId(R.drawable.routine_1)
                    .setFullScreenImageResourceId(R.drawable.routine_1_portrait)
                    .setDescArrayResourceId(R.array.routine_1_desc_array)
                    .createRoutine());
            ROUTINES.add(new Routine.RoutineBuilder()
                    .setStringResourceId(R.string.routine_2)
                    .setImageResourceId(R.drawable.routine_2)
                    .setFullScreenImageResourceId(R.drawable.routine_2_portrait)
                    .setDescArrayResourceId(R.array.routine_2_desc_array).createRoutine());
            ROUTINES.add(new Routine.RoutineBuilder()
                    .setStringResourceId(R.string.routine_3)
                    .setImageResourceId(R.drawable.routine_3)
                    .setFullScreenImageResourceId(R.drawable.routine_3_portrait)
                    .setDescArrayResourceId(R.array.routine_3_desc_array).createRoutine());
            ROUTINES.add(new Routine.RoutineBuilder()
                    .setStringResourceId(R.string.routine_4)
                    .setImageResourceId(R.drawable.routine_4)
                    .setFullScreenImageResourceId(R.drawable.routine_4_portrait)
                    .setDescArrayResourceId(R.array.routine_4_desc_array).createRoutine());
            ROUTINES.add(new Routine.RoutineBuilder()
                    .setStringResourceId(R.string.routine_5)
                    .setImageResourceId(R.drawable.routine_5)
                    .setFullScreenImageResourceId(R.drawable.routine_5_portrait)
                    .setDescArrayResourceId(R.array.routine_5_desc_array).createRoutine());
        }
        return ROUTINES;
    }
}
