package com.snookerup.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.snookerup.data.routines.RoutineRepository;
import com.snookerup.databinding.FragmentHomeBinding;
import com.snookerup.model.Routine;

import java.util.List;

/**
 * Fragment for the home screen, showing all available practice routines.
 *
 * @author Huwdunnit
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getName();

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.allRoutinesRecyclerView;
        recyclerView.setHasFixedSize(true);

        RoutineRepository routineRepository = new RoutineRepository(getContext());
        LiveData<List<Routine>> liveDataRoutines = routineRepository.getAllRoutines();
        //When data is received in LiveData, update the recycler view
        liveDataRoutines.observe(getViewLifecycleOwner(), routines -> {
            Log.d(TAG, "Routines set in LiveData, routines=" + routines);
            recyclerView.setAdapter(new RoutineOverviewItemAdapter(getContext(), routines));
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}