package com.huwdunnit.snookerimprover.ui.addscore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.huwdunnit.snookerimprover.databinding.FragmentAddScoreBinding;

/**
 * Fragment for adding a new score for a routine.
 *
 * @author Huwdunnit
 */
public class AddScoreFragment extends Fragment {

    private FragmentAddScoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddScoreViewModel addScoreViewModel =
                new ViewModelProvider(this).get(AddScoreViewModel.class);

        binding = FragmentAddScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAddScore;
        addScoreViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}