package com.huwdunnit.snookerimprover.ui.common;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.data.Datasource;
import com.huwdunnit.snookerimprover.model.Routine;

/**
 * Handles the changing of the selected routine on a fragment.
 *
 * @author Huwdunnit
 */
public class ChangeableRoutineHandler implements AdapterView.OnItemSelectedListener {

    private final Context context;

    private final ChangeableRoutineViewModel viewModel;

    private final RoutineChangeCallback callback;

    private final Spinner spinner;

    private final Button addScoreButton;

    private final Button viewStatsButton;

    private final Button viewInfoButton;

    private int routineNumber = 0;

    /**
     * Private constructor, to prevent direct instantiation (the internal HandlerBuilder should be
     * used instead).
     */
    private ChangeableRoutineHandler(Context context, ChangeableRoutineViewModel viewModel,
                                     RoutineChangeCallback callback, Spinner spinner,
                                     Button addScoreButton, Button viewStatsButton,
                                     Button viewInfoButton, int startingRoutineNumber) {
        this.context = context;
        this.viewModel = viewModel;
        this.callback = callback;
        this.spinner = spinner;
        this.addScoreButton = addScoreButton;
        this.viewStatsButton = viewStatsButton;
        this.viewInfoButton = viewInfoButton;
        this.routineNumber = startingRoutineNumber;
    }

    public void setupHandling() {
        //Set up the Spinner for selecting the routine
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.all_routine_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Add on-click listeners for the two buttons
        if (addScoreButton != null) {
            addScoreButton.setOnClickListener(view -> callback.navigateToAddScoreScreen(routineNumber));
        }
        if (viewStatsButton != null) {
            viewStatsButton.setOnClickListener(view -> callback.navigateToStatsScreen(routineNumber));
        }
        if (viewInfoButton != null) {
            viewInfoButton.setOnClickListener(view -> callback.navigateToInfoScreen(routineNumber));
        }

        //Set the routine; either the default routine or the one passed in
        updateRoutine();
    }

    /**
     * Update the routine to the routine matching the member routineNumber variable.
     */
    private void updateRoutine() {
        Routine routine = Datasource.getRoutines().get(routineNumber);
        spinner.setSelection(routineNumber);
        viewModel.setRoutine(routine, context);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        this.routineNumber = i;
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
        Context context;
        ChangeableRoutineViewModel viewModel;
        RoutineChangeCallback callback;
        Spinner spinner;
        Button addScoreButton;
        Button viewStatsButton;
        Button viewInfoButton;
        int startingRoutineNumber;

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

        public ChangeableRoutineHandler.HandlerBuilder setStartingRoutineNumber(int startingRoutineNumber) {
            this.startingRoutineNumber = startingRoutineNumber;
            return this;
        }

        public ChangeableRoutineHandler createHandler() {
            return new ChangeableRoutineHandler(context, viewModel, callback, spinner,
                    addScoreButton, viewStatsButton, viewInfoButton, startingRoutineNumber);
        }
    }
}
