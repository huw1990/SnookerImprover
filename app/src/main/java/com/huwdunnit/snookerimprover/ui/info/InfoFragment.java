package com.huwdunnit.snookerimprover.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.data.Datasource;
import com.huwdunnit.snookerimprover.databinding.FragmentInfoBinding;
import com.huwdunnit.snookerimprover.model.Routine;

/**
 * Fragment for getting more detailed information about a routine.
 *
 * @author Huwdunnit
 */
public class InfoFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentInfoBinding binding;

    private InfoViewModel infoViewModel;

    private int routineNumber = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Set up the Spinner for selecting the routine
        Spinner routineNameSpinner = binding.routineNameSpinner;
        routineNameSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.all_routine_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        routineNameSpinner.setAdapter(adapter);

        //Set the LiveData members on the ViewModel to update the relevant UI components
        infoViewModel.getRoutineImageResId().observe(getViewLifecycleOwner(), binding.routineImage::setImageResource);
        infoViewModel.getRoutineDesc().observe(getViewLifecycleOwner(), binding.routineDesc::setText);

        //Add on-click listeners for the two buttons
        binding.buttonAddScore.setOnClickListener(view -> Toast.makeText(getContext(), "addScore clicked", Toast.LENGTH_SHORT).show());
        binding.buttonViewStats.setOnClickListener(view -> Toast.makeText(getContext(), "viewStats clicked", Toast.LENGTH_SHORT).show());

        //Set the routine; either the default routine or the one passed in
        updateRoutine();
        return root;
    }

    /**
     * Update the routine to the routine matching the member routineNumber variable.
     */
    private void updateRoutine() {
        Routine routine = Datasource.getRoutines().get(routineNumber);
        binding.routineNameSpinner.setSelection(routineNumber);
        infoViewModel.setRoutine(routine, getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routineNumber = InfoFragmentArgs.fromBundle(getArguments()).getRoutineNumber();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
}