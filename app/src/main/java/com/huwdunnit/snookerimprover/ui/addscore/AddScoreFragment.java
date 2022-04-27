package com.huwdunnit.snookerimprover.ui.addscore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.data.ScoreRepository;
import com.huwdunnit.snookerimprover.databinding.FragmentAddScoreBinding;
import com.huwdunnit.snookerimprover.model.RoutineScore;
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

    private static final String TAG = AddScoreFragment.class.getName();

    private FragmentAddScoreBinding binding;

    private AddScoreViewModel addScoreViewModel;

    // Handler for the common UI components, e.g. the image and spinner to change routine
    private ChangeableRoutineHandler routineChangeHandler;

    private int startingRoutineNumber = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addScoreViewModel = new ViewModelProvider(this).get(AddScoreViewModel.class);

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

        binding.submitScoreButton.setOnClickListener(view -> {
            Log.d(TAG, "Validating score before submitting");
            //Validate the score field
            int scoreValue = -1;
            boolean notANumber = false;
            try {
                scoreValue = Integer.parseInt(binding.enteredScore.getText().toString());
            } catch (NumberFormatException ex) {
                notANumber = true;
            }
            if (scoreValue < 0 || notANumber) {
                Log.e(TAG, "Score not valid, value=" + binding.enteredScore.getText());
                Toast.makeText(getContext(), R.string.enter_valid_score_help, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            Log.d(TAG, "Validation successful, submitting score now");

            //Validation passed, now enter the score in the DB
            ScoreRepository repo = new ScoreRepository(getContext());
            RoutineScore routineScore = new RoutineScore();
            routineScore.setScore(scoreValue);
            routineScore.setRoutineName(routineChangeHandler.getSelectedRoutineName());
            routineScore.setDateTime(addScoreViewModel.getFullDateAndTime());
            Log.e(TAG, "Score to submit=" + routineScore);
            repo.insert(routineScore, () -> {
                showInsertResult(view, true);
            }, () -> {
                showInsertResult(view, false);
            });
        });

        return root;
    }

    private void showInsertResult(View view, boolean success) {
        //Hide the soft keyboard
        InputMethodManager inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        //Show a snackbar to tell the user the operation was successful
        int stringResId = R.string.success_message;
        if (!success) {
            stringResId = R.string.failure_try_again_message;
        }
        Snackbar snackbar = Snackbar.make(view, stringResId, Snackbar.LENGTH_LONG);
        snackbar.show();
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
        Navigation.findNavController(requireView()).navigate(action,
                new NavOptions.Builder().setPopUpTo(R.id.navigation_add_score, true).build());
    }

    @Override
    public void navigateToInfoScreen(int routineNumber) {
        AddScoreFragmentDirections.ActionAddScoreToInfo action = AddScoreFragmentDirections.actionAddScoreToInfo();
        action.setRoutineNumber(routineNumber);
        Navigation.findNavController(requireView()).navigate(action,
                new NavOptions.Builder().setPopUpTo(R.id.navigation_add_score, true).build());
    }

    @Override
    public void navigateToAddScoreScreen(int routineNumber) {
        // Do nothing, we're already on the add score screen
    }
}