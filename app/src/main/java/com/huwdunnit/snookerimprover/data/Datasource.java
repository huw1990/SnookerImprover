package com.huwdunnit.snookerimprover.data;

import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.model.Routine;

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
            ROUTINES.add(new Routine(R.string.routine_1, R.drawable.routine_1, R.array.routine_1_desc_array));
            ROUTINES.add(new Routine(R.string.routine_2, R.drawable.routine_2, R.array.routine_2_desc_array));
            ROUTINES.add(new Routine(R.string.routine_3, R.drawable.routine_3, R.array.routine_3_desc_array));
            ROUTINES.add(new Routine(R.string.routine_4, R.drawable.routine_4, R.array.routine_4_desc_array));
            ROUTINES.add(new Routine(R.string.routine_5, R.drawable.routine_5, R.array.routine_5_desc_array));
        }
        return ROUTINES;
    }
}
