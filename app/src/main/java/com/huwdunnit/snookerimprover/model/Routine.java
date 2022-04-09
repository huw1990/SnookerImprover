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

    /** The Image resource ID for the routine in full-screen. */
    @DrawableRes
    private final int fullScreenImageResourceId;

    /** The String array resource ID for the various description lines for this routine. */
    @ArrayRes
    private final int descArrayResourceId;

    /**
     * Private constructor, to prevent direct instantiation (the internal RoutineBuilder should be
     * used instead).
     */
    private Routine(int stringResourceId, int imageResourceId, int fullScreenImageResourceId, int descArrayResourceId) {
        this.stringResourceId = stringResourceId;
        this.imageResourceId = imageResourceId;
        this.fullScreenImageResourceId = fullScreenImageResourceId;
        this.descArrayResourceId = descArrayResourceId;
    }

    public int getStringResourceId() {
        return stringResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getFullScreenImageResourceId() {
        return fullScreenImageResourceId;
    }

    public int getDescArrayResourceId() {
        return descArrayResourceId;
    }

    /**
     * A Builder for creating Routines.
     *
     * @author Huwdunnit
     */
    public static class RoutineBuilder {
        private int stringResourceId;
        private int imageResourceId;
        private int fullScreenImageResourceId;
        private int descArrayResourceId;

        public RoutineBuilder setStringResourceId(int stringResourceId) {
            this.stringResourceId = stringResourceId;
            return this;
        }

        public RoutineBuilder setImageResourceId(int imageResourceId) {
            this.imageResourceId = imageResourceId;
            return this;
        }

        public RoutineBuilder setFullScreenImageResourceId(int fullScreenImageResourceId) {
            this.fullScreenImageResourceId = fullScreenImageResourceId;
            return this;
        }

        public RoutineBuilder setDescArrayResourceId(int descArrayResourceId) {
            this.descArrayResourceId = descArrayResourceId;
            return this;
        }

        public Routine createRoutine() {
            return new Routine(stringResourceId, imageResourceId, fullScreenImageResourceId, descArrayResourceId);
        }
    }
}
