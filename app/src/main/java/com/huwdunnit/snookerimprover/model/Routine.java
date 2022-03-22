package com.huwdunnit.snookerimprover.model;

/**
 * Maps a practice routine available in the app.
 *
 * @author Huwdunnit
 */
public class Routine {

    /** The String resource ID for the title of the routine. */
    private final int stringResourceId;

    public Routine(int stringResourceId) {
        this.stringResourceId = stringResourceId;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }
}
