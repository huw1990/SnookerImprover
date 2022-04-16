package com.huwdunnit.snookerimprover.ui.addscore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.databinding.FragmentAddScoreBinding;
import com.huwdunnit.snookerimprover.ui.common.ChangeableRoutineHandler;
import com.huwdunnit.snookerimprover.ui.common.DatePickerFragment;
import com.huwdunnit.snookerimprover.ui.common.RoutineChangeCallback;
import com.huwdunnit.snookerimprover.ui.common.TimePickerFragment;

/**
 * Fragment for adding a new score for a routine.
 *
 * @author Huwdunnit
 */
public class AddScoreFragment extends Fragment implements RoutineChangeCallback {

    private FragmentAddScoreBinding binding;

    // Handler for the common UI components, e.g. the image and spinner to change routine
    private ChangeableRoutineHandler routineChangeHandler;

    private int startingRoutineNumber = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddScoreViewModel addScoreViewModel = new ViewModelProvider(this).get(AddScoreViewModel.class);

        binding = FragmentAddScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Set up the common handler for being able to change the routine with a dropdown
        routineChangeHandler = new ChangeableRoutineHandler.HandlerBuilder()
                .setContext(getContext())
                .setViewModel(addScoreViewModel)
                .setCallback(this)
                .setSpinner(binding.routineNameSpinner)
                .setViewInfoButton(binding.buttonViewInfo)
                .setViewStatsButton(binding.buttonViewStats)
                .setStartingRoutineNumber(startingRoutineNumber)
                .createHandler();
        routineChangeHandler.setupHandling();

        //Track the changes to the score field
        addScoreViewModel.getScore().observe(getViewLifecycleOwner(), binding.enteredScore::setText);

        //Handle the date field, by showing a date picker on click, and formatting the entered date
        addScoreViewModel.setDateFormat(getContext().getResources().getString(R.string.date_format));
        addScoreViewModel.setTodayLabel(getContext().getResources().getString(R.string.today_label));
        addScoreViewModel.getDate().observe(getViewLifecycleOwner(), binding.enteredDate::setText);
        binding.enteredDate.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment(addScoreViewModel);
            newFragment.show(getParentFragmentManager(), "datePicker");
        });

        //Handle the time field, by showing a time picker on click, and formatting the entered time
        addScoreViewModel.getTime().observe(getViewLifecycleOwner(), binding.enteredTime::setText);
        binding.enteredTime.setOnClickListener(view -> {
            DialogFragment newFragment = new TimePickerFragment(addScoreViewModel);
            newFragment.show(getParentFragmentManager(), "timePicker");
        });

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingRoutineNumber = AddScoreFragmentArgs.fromBundle(getArguments()).getRoutineNumber();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        routineChangeHandler = null;
    }

    @Override
    public void navigateToStatsScreen(int routineNumber) {
        AddScoreFragmentDirections.ActionAddScoreToStats action = AddScoreFragmentDirections.actionAddScoreToStats();
        action.setRoutineNumber(routineNumber);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void navigateToInfoScreen(int routineNumber) {
        AddScoreFragmentDirections.ActionAddScoreToInfo action = AddScoreFragmentDirections.actionAddScoreToInfo();
        action.setRoutineNumber(routineNumber);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void navigateToAddScoreScreen(int routineNumber) {
        // Do nothing, we're already on the add score screen
    }
}