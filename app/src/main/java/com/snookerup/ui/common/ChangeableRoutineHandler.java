package com.snookerup.ui.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.snookerup.data.routines.RoutineRepository;

import java.util.List;

/**
 * Handles the changing of the selected routine on a fragment.
 *
 * @author Huwdunnit
 */
public class ChangeableRoutineHandler implements AdapterView.OnItemSelectedListener {

    private static final String TAG = ChangeableRoutineHandler.class.getName();

    private final Context context;

    private final ChangeableRoutineViewModel viewModel;

    private final RoutineChangeCallback callback;

    private final Spinner spinner;

    private final Button addScoreButton;

    private final Button viewStatsButton;

    private final Button viewInfoButton;

    private final RoutineRepository routineRepository;

    private final Activity activity;

    private List<String> allRoutineNames;

    private String selectedRoutineName;

    /**
     * Private constructor, to prevent direct instantiation (the internal HandlerBuilder should be
     * used instead).
     */
    private ChangeableRoutineHandler(Activity activity, Context context, ChangeableRoutineViewModel viewModel,
                                     RoutineChangeCallback callback, Spinner spinner,
                                     Button addScoreButton, Button viewStatsButton,
                                     Button viewInfoButton, String selectedRoutineTitle) {
        this.activity = activity;
        this.context = context;
        this.viewModel = viewModel;
        this.callback = callback;
        this.spinner = spinner;
        this.addScoreButton = addScoreButton;
        this.viewStatsButton = viewStatsButton;
        this.viewInfoButton = viewInfoButton;
        this.selectedRoutineName = selectedRoutineTitle;
        this.routineRepository = new RoutineRepository(context);
    }

    /**
     * Get the title of the currently selected routine.
     * @return The title of the selected routine
     */
    public String getSelectedRoutineName() {
        return selectedRoutineName;
    }

    public void setupHandling() {
        //Add on-click listeners for the two buttons
        if (addScoreButton != null) {
            addScoreButton.setOnClickListener(view -> callback.navigateToAddScoreScreen(selectedRoutineName));
        }
        if (viewStatsButton != null) {
            viewStatsButton.setOnClickListener(view -> callback.navigateToStatsScreen(selectedRoutineName));
        }
        if (viewInfoButton != null) {
            viewInfoButton.setOnClickListener(view -> callback.navigateToInfoScreen(selectedRoutineName));
        }

        routineRepository.getAllRoutineNames(routines -> {
            this.allRoutineNames = routines;
            if (this.selectedRoutineName == null) {
                //Default the selected routine to the first in the list, so we have something to set the spinner to
                this.selectedRoutineName = allRoutineNames.get(0);
            }
            //Set up the Spinner for selecting the routine
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter<String> adapter = new ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, allRoutineNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            //Set the routine; either the default routine or the one passed in
            updateRoutine();
        }, ex -> {
            Log.e(TAG, "Can't get all routine titles, ex=" + ex);
            ex.printStackTrace();
        });
    }

    /**
     * Update the routine to the routine matching the member routineNumber variable.
     */
    private void updateRoutine() {
        routineRepository.getRoutineFromName(selectedRoutineName, routine -> {
            int indexOfName = allRoutineNames.indexOf(selectedRoutineName);
            activity.runOnUiThread(() -> {
                spinner.setSelection(indexOfName);
                viewModel.setRoutine(routine, context);
            });
        }, ex -> {
            Log.e(TAG, "Can't get all routine from name=" + selectedRoutineName + ", ex=" + ex);
            ex.printStackTrace();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.selectedRoutineName = allRoutineNames.get(i);
        updateRoutine();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }

    /**
     * A Builder for creating ChangeableRoutineHandler objects.
     *
     * @author Huwdunnit
     */
    public static class HandlerBuilder {
        Activity activity;
        Context context;
        ChangeableRoutineViewModel viewModel;
        RoutineChangeCallback callback;
        Spinner spinner;
        Button addScoreButton;
        Button viewStatsButton;
        Button viewInfoButton;
        String startingRoutineName;

        public ChangeableRoutineHandler.HandlerBuilder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setContext(Context context) {
            this.context = context;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setViewModel(ChangeableRoutineViewModel viewModel) {
            this.viewModel = viewModel;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setCallback(RoutineChangeCallback callback) {
            this.callback = callback;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setSpinner(Spinner spinner) {
            this.spinner = spinner;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setAddScoreButton(Button addScoreButton) {
            this.addScoreButton = addScoreButton;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setViewStatsButton(Button viewStatsButton) {
            this.viewStatsButton = viewStatsButton;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setViewInfoButton(Button viewInfoButton) {
            this.viewInfoButton = viewInfoButton;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setStartingRoutineName(String startingRoutineName) {
            this.startingRoutineName = startingRoutineName;
            return this;
        }

        public ChangeableRoutineHandler createHandler() {
            return new ChangeableRoutineHandler(activity, context, viewModel, callback, spinner,
                    addScoreButton, viewStatsButton, viewInfoButton, startingRoutineName);
        }
    }
}
