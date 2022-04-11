package com.huwdunnit.snookerimprover.ui.common;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.lifecycle.LifecycleOwner;

import com.huwdunnit.snookerimprover.FullscreenRoutineImageActivity;
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

    private final LifecycleOwner lifecycleOwner;

    private final ChangeableRoutineViewModel viewModel;

    private final RoutineChangeCallback callback;

    private final Spinner spinner;

    private final ImageView routineImageView;

    private final Button addScoreButton;

    private final Button viewStatsButton;

    private final Button viewInfoButton;

    private int routineNumber = 0;

    /**
     * Private constructor, to prevent direct instantiation (the internal HandlerBuilder should be
     * used instead).
     */
    private ChangeableRoutineHandler(Context context, LifecycleOwner lifecycleOwner,
                                     ChangeableRoutineViewModel viewModel,
                                     RoutineChangeCallback callback, Spinner spinner,
                                     ImageView routineImageView, Button addScoreButton,
                                     Button viewStatsButton, Button viewInfoButton,
                                     int startingRoutineNumber) {
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.viewModel = viewModel;
        this.callback = callback;
        this.spinner = spinner;
        this.routineImageView = routineImageView;
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

        //Set the LiveData members on the ViewModel to update the relevant UI components
        viewModel.getRoutineImageResId().observe(lifecycleOwner, routineImageView::setImageResource);

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

        routineImageView.setOnClickListener(view -> {
            //When the user clicks on the image of the routine, make it fullscreen
            Intent intent = new Intent(context, FullscreenRoutineImageActivity.class);
            intent.putExtra(FullscreenRoutineImageActivity.IMAGE_RES_ID, viewModel.getRoutineFullScreenImageResId().getValue());
            context.startActivity(intent);
        });

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
        LifecycleOwner lifecycleOwner;
        ChangeableRoutineViewModel viewModel;
        RoutineChangeCallback callback;
        Spinner spinner;
        ImageView routineImageView;
        Button addScoreButton;
        Button viewStatsButton;
        Button viewInfoButton;
        int startingRoutineNumber;

        public ChangeableRoutineHandler.HandlerBuilder setContext(Context context) {
            this.context = context;
            return this;
        }

        public ChangeableRoutineHandler.HandlerBuilder setLifecycleOwner(LifecycleOwner lifecycleOwner) {
            this.lifecycleOwner = lifecycleOwner;
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

        public ChangeableRoutineHandler.HandlerBuilder setRoutineImageView(ImageView routineImageView) {
            this.routineImageView = routineImageView;
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
            return new ChangeableRoutineHandler(context, lifecycleOwner, viewModel, callback,
                    spinner, routineImageView, addScoreButton, viewStatsButton,
                    viewInfoButton, startingRoutineNumber);
        }
    }
}
