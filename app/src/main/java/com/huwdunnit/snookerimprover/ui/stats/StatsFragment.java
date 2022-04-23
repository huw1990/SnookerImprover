package com.huwdunnit.snookerimprover.ui.stats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.data.ScoreRepository;
import com.huwdunnit.snookerimprover.databinding.FragmentStatsBinding;
import com.huwdunnit.snookerimprover.ui.common.ChangeableRoutineHandler;
import com.huwdunnit.snookerimprover.ui.common.RoutineChangeCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for getting more detailed information about a routine.
 *
 * @author Huwdunnit
 */
public class StatsFragment extends Fragment implements RoutineChangeCallback,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = StatsFragment.class.getName();

    private FragmentStatsBinding binding;

    private StatsViewModel statsViewModel;

    // Handler for the common UI components, e.g. the image and spinner to change routine
    private ChangeableRoutineHandler routineChangeHandler;

    private int startingRoutineNumber = 0;

    private List<Integer> statsPeriodsDays = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);

        binding = FragmentStatsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Take the time periods as strings, parse as ints, then set in a List
        Log.d(TAG, "Parsing time period strings");
        String[] statsPeriods = getContext().getResources().getStringArray(R.array.time_periods_numbers);
        for (String statsPeriod : statsPeriods) {
            statsPeriodsDays.add(Integer.parseInt(statsPeriod));
        }

        Log.d(TAG, "Setting required values from String resources. Also DB repo");
        statsViewModel.setUnknownLabel(getContext().getString(R.string.unknown_label));
        statsViewModel.setLoadingLabel(getContext().getString(R.string.loading_label));
        statsViewModel.setNoPreviousAttemptsLabel(getContext().getString(R.string.no_previous_attempts_label));
        statsViewModel.setScoreRepository(new ScoreRepository(getContext()));
        statsViewModel.setDateFormat(getContext().getString(R.string.date_format));

        //Set up LiveData for the fields specific to this fragment
        statsViewModel.getNumberOfAttempts().observe(getViewLifecycleOwner(), binding.numberAttemptsValue::setText);
        statsViewModel.getBestScore().observe(getViewLifecycleOwner(), binding.bestValue::setText);
        statsViewModel.getAverageScore().observe(getViewLifecycleOwner(), binding.averageValue::setText);

        //Set up the common handler for being able to change the routine with a dropdown
        routineChangeHandler = new ChangeableRoutineHandler.HandlerBuilder()
                .setContext(getContext())
                .setViewModel(statsViewModel)
                .setCallback(this)
                .setSpinner(binding.routineNameSpinner)
                .setAddScoreButton(binding.buttonAddScore)
                .setViewInfoButton(binding.buttonViewInfo)
                .setStartingRoutineNumber(startingRoutineNumber)
                .createHandler();
        routineChangeHandler.setupHandling();

        //Set up the time period spinner
        binding.statsPeriodSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.time_periods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.statsPeriodSpinner.setAdapter(adapter);

        //Finally, reload the stats on the ViewModel, since we've updated values recently
        statsViewModel.reloadStats();

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingRoutineNumber = StatsFragmentArgs.fromBundle(getArguments()).getRoutineNumber();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        routineChangeHandler = null;
    }

    @Override
    public void navigateToStatsScreen(int routineNumber) {
        // Do nothing, we're already on the info screen
    }

    @Override
    public void navigateToInfoScreen(int routineNumber) {
        StatsFragmentDirections.ActionStatsToInfo action = StatsFragmentDirections.actionStatsToInfo();
        action.setRoutineNumber(routineNumber);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void navigateToAddScoreScreen(int routineNumber) {
        StatsFragmentDirections.ActionStatsToAddScore action = StatsFragmentDirections.actionStatsToAddScore();
        action.setRoutineNumber(routineNumber);
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        statsViewModel.setDaysToView(statsPeriodsDays.get(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }
}