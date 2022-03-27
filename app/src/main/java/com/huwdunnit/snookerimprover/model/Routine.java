package com.huwdunnit.snookerimprover.model;

import androidx.annotation.ArrayRes;
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

    /** The String array resource ID for the various description lines for this routine. */
    @ArrayRes
    private final int descArrayResourceId;

    public Routine(int stringResourceId, int imageResourceId, int descArrayResourceId) {
        this.stringResourceId = stringResourceId;
        this.imageResourceId = imageResourceId;
        this.descArrayResourceId = descArrayResourceId;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getDescArrayResourceId() {
        return descArrayResourceId;
    }
}
