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

    /**
     * Load all hardcoded practice routines from app resources.
     * @return All practice routines
     */
    public List<Routine> loadRoutines() {
        List<Routine> routines = new ArrayList<>();
        routines.add(new Routine(R.string.routine_1, R.drawable.routine_1));
        routines.add(new Routine(R.string.routine_2, R.drawable.routine_2));
        routines.add(new Routine(R.string.routine_3, R.drawable.routine_3));
        routines.add(new Routine(R.string.routine_4, R.drawable.routine_4));
        routines.add(new Routine(R.string.routine_5, R.drawable.routine_5));
        return routines;
    }
}
