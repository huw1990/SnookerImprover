package com.snookerup.ui.common;

/**
 * A callback for fragments that support changing the selected routine.
 *
 * @author Huwdunnit
 */
public interface RoutineChangeCallback {

    /**
     * Navigate to the Stats screen, and show the routine with the selected number.
     * @param routineName The name of the routine to display
     */
    void navigateToStatsScreen(String routineName);

    /**
     * Navigate to the Info screen, and show the routine with the selected number.
     * @param routineName The title of the routine to display
     */
    void navigateToInfoScreen(String routineName);

    /**
     * Navigate to the Add Score screen, and show the routine with the selected number.
     * @param routineName The title of the routine to display
     */
    void navigateToAddScoreScreen(String routineName);
}
