package com.snookerup.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.snookerup.FullscreenRoutineImageActivity;
import com.snookerup.R;
import com.snookerup.databinding.FragmentInfoBinding;
import com.snookerup.ui.common.ChangeableRoutineHandler;
import com.snookerup.ui.common.RoutineChangeCallback;

/**
 * Fragment for getting more detailed information about a routine.
 *
 * @author Huwdunnit
 */
public class InfoFragment extends Fragment implements RoutineChangeCallback {

    private FragmentInfoBinding binding;

    // Handler for the common UI components, e.g. the image and spinner to change routine
    private ChangeableRoutineHandler routineChangeHandler;

    private String startingRoutineName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InfoViewModel infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Set up the common handler for being able to change the routine with a dropdown
        routineChangeHandler = new ChangeableRoutineHandler.HandlerBuilder()
                .setActivity(getActivity())
                .setContext(getContext())
                .setViewModel(infoViewModel)
                .setCallback(this)
                .setSpinner(binding.routineNameSpinner)
                .setAddScoreButton(binding.buttonAddScore)
                .setViewStatsButton(binding.buttonViewStats)
                .setStartingRoutineName(startingRoutineName)
                .createHandler();
        routineChangeHandler.setupHandling();

        //Set up LiveData for the fields specific to this fragment

        infoViewModel.getRoutineImage().observe(getViewLifecycleOwner(), binding.routineImage::setImageDrawable);

        binding.routineImage.setOnClickListener(view -> {
            //When the user clicks on the image of the routine, make it fullscreen
            Intent intent = new Intent(getContext(), FullscreenRoutineImageActivity.class);
            intent.putExtra(FullscreenRoutineImageActivity.IMAGE_FILENAME, infoViewModel.getRoutineFullScreenImageFilename().getValue());
            getContext().startActivity(intent);
        });

        infoViewModel.getRoutineDesc().observe(getViewLifecycleOwner(), binding.routineDesc::setText);
        //Allow the user to scroll through the TextView
        binding.routineDesc.setMovementMethod(new ScrollingMovementMethod());

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingRoutineName = InfoFragmentArgs.fromBundle(getArguments()).getRoutineName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        routineChangeHandler = null;
    }

    @Override
    public void navigateToStatsScreen(String routineName) {
        InfoFragmentDirections.ActionInfoToStats action = InfoFragmentDirections.actionInfoToStats();
        action.setRoutineName(routineName);
        Navigation.findNavController(requireView()).navigate(action,
                new NavOptions.Builder().setPopUpTo(R.id.navigation_info, true).build());
    }

    @Override
    public void navigateToInfoScreen(String routineName) {
        // Do nothing, we're already on the info screen
    }

    @Override
    public void navigateToAddScoreScreen(String routineName) {
        InfoFragmentDirections.ActionInfoToAddScore action = InfoFragmentDirections.actionInfoToAddScore();
        action.setRoutineName(routineName);
        Navigation.findNavController(requireView()).navigate(action,
                new NavOptions.Builder().setPopUpTo(R.id.navigation_info, true).build());
    }
}