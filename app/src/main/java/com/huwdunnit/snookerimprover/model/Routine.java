package com.huwdunnit.snookerimprover.model;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

/**
 * Maps a practice routine available in the app.
 *
 * @author Huwdunnit
 */
public class Routine {

    /** The String resource ID for the title of the routine. */
    @StringRes
    private final int stringResourceId;

    /** The Image resource ID for the routine. */
    @DrawableRes
    private final int imageResourceId;

    public Routine(int stringResourceId, int imageResourceId) {
        this.stringResourceId = stringResourceId;
        this.imageResourceId = imageResourceId;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
