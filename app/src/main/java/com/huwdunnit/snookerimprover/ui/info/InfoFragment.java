package com.huwdunnit.snookerimprover.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.huwdunnit.snookerimprover.FullscreenRoutineImageActivity;
import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.databinding.FragmentInfoBinding;
import com.huwdunnit.snookerimprover.ui.common.ChangeableRoutineHandler;
import com.huwdunnit.snookerimprover.ui.common.RoutineChangeCallback;

import java.util.Objects;

/**
 * Fragment for getting more detailed information about a routine.
 *
 * @author Huwdunnit
 */
public class InfoFragment extends Fragment implements RoutineChangeCallback {

    private FragmentInfoBinding binding;

    // Handler for the common UI components, e.g. the image and spinner to change routine
    private ChangeableRoutineHandler routineChangeHandler;

    private int startingRoutineNumber = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InfoViewModel infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Set up the common handler for being able to change the routine with a dropdown
        routineChangeHandler = new ChangeableRoutineHandler.HandlerBuilder()
                .setContext(getContext())
                .setViewModel(infoViewModel)
                .setCallback(this)
                .setSpinner(binding.routineNameSpinner)
                .setAddScoreButton(binding.buttonAddScore)
                .setViewStatsButton(binding.buttonViewStats)
                .setStartingRoutineNumber(startingRoutineNumber)
                .createHandler();
        routineChangeHandler.setupHandling();

        //Set up LiveData for the fields specific to this fragment

        infoViewModel.getRoutineImageResId().observe(getViewLifecycleOwner(), binding.routineImage::setImageResource);

        binding.routineImage.setOnClickListener(view -> {
            //When the user clicks on the image of the routine, make it fullscreen
            Intent intent = new Intent(getContext(), FullscreenRoutineImageActivity.class);
            intent.putExtra(FullscreenRoutineImageActivity.IMAGE_RES_ID, infoViewModel.getRoutineFullScreenImageResId().getValue());
            getContext().startActivity(intent);
        });

        infoViewModel.getRoutineDesc().observe(getViewLifecycleOwner(), binding.routineDesc::setText);

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingRoutineNumber = InfoFragmentArgs.fromBundle(getArguments()).getRoutineNumber();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        routineChangeHandler = null;
    }

    @Override
    public void navigateToStatsScreen(int routineNumber) {
        InfoFragmentDirections.ActionInfoToStats action = InfoFragmentDirections.actionInfoToStats();
        action.setRoutineNumber(routineNumber);
        Navigation.findNavController(requireView()).navigate(action,
                new NavOptions.Builder().setPopUpTo(R.id.navigation_info, true).build());
    }

    @Override
    public void navigateToInfoScreen(int routineNumber) {
        // Do nothing, we're already on the info screen
    }

    @Override
    public void navigateToAddScoreScreen(int routineNumber) {
        InfoFragmentDirections.ActionInfoToAddScore action = InfoFragmentDirections.actionInfoToAddScore();
        action.setRoutineNumber(routineNumber);
        Navigation.findNavController(requireView()).navigate(action,
                new NavOptions.Builder().setPopUpTo(R.id.navigation_info, true).build());
    }
}