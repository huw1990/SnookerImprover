package com.huwdunnit.snookerimprover.ui.addscore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.huwdunnit.snookerimprover.databinding.FragmentAddScoreBinding;
import com.huwdunnit.snookerimprover.ui.common.ChangeableRoutineHandler;
import com.huwdunnit.snookerimprover.ui.common.RoutineChangeCallback;

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
                .setLifecycleOwner(getViewLifecycleOwner())
                .setViewModel(addScoreViewModel)
                .setCallback(this)
                .setSpinner(binding.routineNameSpinner)
                .setRoutineImageView(binding.routineImage)
                .setViewInfoButton(binding.buttonViewInfo)
                .setViewStatsButton(binding.buttonViewStats)
                .setStartingRoutineNumber(startingRoutineNumber)
                .createHandler();
        routineChangeHandler.setupHandling();

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