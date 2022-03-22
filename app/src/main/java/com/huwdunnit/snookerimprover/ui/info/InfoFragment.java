package com.huwdunnit.snookerimprover.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.huwdunnit.snookerimprover.databinding.FragmentInfoBinding;

/**
 * Fragment for getting more detailed information about a routine.
 *
 * @author Huwdunnit
 */
public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;

    private int titleStringResId = -1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InfoViewModel infoViewModel =
                new ViewModelProvider(this).get(InfoViewModel.class);

        binding = FragmentInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textInfo;
        if (titleStringResId != -1) {
            infoViewModel.setText(getResources().getString(titleStringResId));
        }
        infoViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleStringResId = InfoFragmentArgs.fromBundle(getArguments()).getRoutineTitleStringResId();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}