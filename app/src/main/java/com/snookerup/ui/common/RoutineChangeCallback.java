package com.snookerup.ui.common;

/**
 * A callback for fragments that support changing the selected routine.
 *
 * @author Huwdunnit
 */
public interface RoutineChangeCallback {

    /**
     * Navigate to the Stats screen, and show the routine with the selected number.
     * @param routineNumber The number of the routine to display
     */
    void navigateToStatsScreen(int routineNumber);

    /**
     * Navigate to the Info screen, and show the routine with the selected number.
     * @param routineNumber The number of the routine to display
     */
    void navigateToInfoScreen(int routineNumber);

    /**
     * Navigate to the Add Score screen, and show the routine with the selected number.
     * @param routineNumber The number of the routine to display
     */
    void navigateToAddScoreScreen(int routineNumber);
}
