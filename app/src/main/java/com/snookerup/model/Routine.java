package com.snookerup.model;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * Maps a practice routine available in the app, stored in the DB.
 *
 * @author Huwdunnit
 */
@Entity
public class Routine {

    private static final String TAG = Routine.class.getName();

    /** Root directory for practice routine images. */
    private static final String ROUTINE_IMAGE_LOCATION_PREFIX = "routines/images/";

    /** Filename suffix for the portrait version of a routine image. */
    private static final String ROUTINE_PORTRAIT_IMAGE_SUFFIX = "_p";

    /** Filename suffix for the landscape version of a routine image. */
    private static final String ROUTINE_LANDSCAPE_IMAGE_SUFFIX = "_ls";

    /** File extension for routine images. */
    private static final String ROUTINE_IMAGE_EXTENSION = ".png";

    @PrimaryKey (autoGenerate = true)
    private int id;

    @NonNull
    private String name;

    @SerializedName("description_lines")
    private List<String> descriptionLines;

    @SerializedName("image_ref")
    private String imageRef;

    public Routine() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDescriptionLines() {
        return descriptionLines;
    }

    public void setDescriptionLines(List<String> descriptionLines) {
        this.descriptionLines = descriptionLines;
    }

    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    /**
     * Get the filename for the routine image in portrait, relative to the app's assets directory.
     * @return The routine portrait image file name
     */
    public String getPortraitImageFileName() {
        return ROUTINE_IMAGE_LOCATION_PREFIX + imageRef + ROUTINE_PORTRAIT_IMAGE_SUFFIX + ROUTINE_IMAGE_EXTENSION;
    }

    /**
     * Get the routine portrait image as a Drawable, using the provided asset manager.
     * @param assets The asset manager to pull the image from the app's assets directory
     * @return The routine portrait image, as a Drawable
     */
    public Drawable getPortraitImage(AssetManager assets) {
        return getDrawableForFilePath(assets, getPortraitImageFileName());
    }

    /**
     * Get the filename for the routine image in landscape, relative to the app's assets directory.
     * @return The routine landscape image file name
     */
    public String getLandscapeImageFileName() {
        return ROUTINE_IMAGE_LOCATION_PREFIX + imageRef + ROUTINE_LANDSCAPE_IMAGE_SUFFIX + ROUTINE_IMAGE_EXTENSION;
    }

    public Drawable getLandscapeImage(AssetManager assets) {
        return getDrawableForFilePath(assets, getLandscapeImageFileName());
    }

    @Override
    public String toString() {
        return "Routine{" +
                "title='" + name + '\'' +
                ", descriptionLines=" + descriptionLines +
                ", imageRef='" + imageRef + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Routine routine = (Routine) o;
        return Objects.equals(name, routine.name) && Objects.equals(descriptionLines, routine.descriptionLines) && Objects.equals(imageRef, routine.imageRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, descriptionLines, imageRef);
    }

    /**
     * Convenience method for getting a Drawable resource from a file in the app's assets folder.
     * @param assets The app's assets manager
     * @param filename The name of the file to convert to drawable
     * @return A Drawable resource for the provided filename, or null if there was an exception
     *         reading the file
     */
    public static Drawable getDrawableForFilePath(AssetManager assets, String filename) {
        try {
            InputStream inputStream = assets.open(filename);
            return Drawable.createFromStream(inputStream, null);
        } catch (IOException ex) {
            Log.e(TAG, "Error getting drawable resource from asset filename=" + filename + ", ex=" + ex);
            ex.printStackTrace();
            return null;
        }
    }
}
