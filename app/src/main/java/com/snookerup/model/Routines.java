package com.snookerup.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Manages the starting routines that will be stored in the app's database.
 *
 * @author Huwdunnit
 */
public class Routines {

    private static final String TAG = Routines.class.getName();

    /** The location in the assets directory of the JSON file containing the routine information. */
    private static final String ROUTINES_JSON_ASSET_FILENAME = "routines/starting_routines.json";

    List<Routine> routines;

    /**
     * Parse the starting routines JSON file (in the app's assets) into a collection of routines.
     * @param assets The app's assets manager
     * @return An object containing a list of practice routines parsed from the file
     */
    public static Routines parseRoutinesFromAssets(AssetManager assets) {
        Log.d(TAG, "Parsing routines from assets file at " + ROUTINES_JSON_ASSET_FILENAME);
        String jsonString;
        try (InputStream is = assets.open(ROUTINES_JSON_ASSET_FILENAME);) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.e(TAG, "Unable to parse routines from assets, exception=" + ex);
            ex.printStackTrace();
            return null;
        }

        Log.d(TAG, "Parsing into Routines object, raw JSON=" + jsonString);
        Gson gson = new Gson();
        Routines routines = gson.fromJson(jsonString, Routines.class);
        Log.d(TAG, "Parsed routines=" + routines);
        return routines;
    }

    public List<Routine> getRoutines() {
        return routines;
    }

    public void setRoutines(List<Routine> routines) {
        this.routines = routines;
    }

    @Override
    public String toString() {
        return "Routines{" +
                "routines=" + routines +
                '}';
    }
}
