package com.huwdunnit.snookerimprover.ui.stats.allscores;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.data.Datasource;
import com.huwdunnit.snookerimprover.data.ScoreRepository;
import com.huwdunnit.snookerimprover.databinding.ActivityRoutineScoresListBinding;
import com.huwdunnit.snookerimprover.model.Routine;
import com.huwdunnit.snookerimprover.model.RoutineScore;

import java.util.Set;

/**
 * An Activity that displays all of the user's previous attempts at a particular routine.
 *
 * @author Huwdunnit
 */
public class RoutineScoresListActivity extends AppCompatActivity {

    /** Key to get the routine we want to load. */
    public static final String SELECTED_ROUTINE_ID = "com.huwdunnit.snookerimprover.SELECTED_ROUTINE_ID";

    private static final String TAG = RoutineScoresListActivity.class.getName();

    private ActivityRoutineScoresListBinding binding;

    private RoutineScoresViewModel attemptsViewModel;

    private ScoreRepository scoreRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scoreRepository = new ScoreRepository(this);

        binding = ActivityRoutineScoresListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.deleteSelectedButton.setOnClickListener(view -> {
            Log.d(TAG, "User deleting actions, ask them to confirm first");
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.delete_scores_question);
            alertDialogBuilder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                Set<RoutineScore> scoresToDelete = attemptsViewModel.getScoresToDelete();
                scoreRepository.deleteMultipleScores(scoresToDelete, numDeleted -> {
                    attemptsViewModel.clearSelectedScores();
                    showDeleteResult(view, true, numDeleted);
                }, () -> {
                    showDeleteResult(view, false, 0);
                });
            });
            alertDialogBuilder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
                Log.d(TAG, "User cancelled delete action");
            });
            alertDialogBuilder.create().show();
        });

        attemptsViewModel = new ViewModelProvider(this).get(RoutineScoresViewModel.class);
        attemptsViewModel.setNoneSelectedString(getString(R.string.select_to_delete_label));
        attemptsViewModel.setSelectedString(getString(R.string.selected_label));

        Intent intent = getIntent();
        int routineNumber = intent.getIntExtra(SELECTED_ROUTINE_ID, 0);
        Routine routine = Datasource.getRoutines().get(routineNumber);
        String routineName = getString(routine.getStringResourceId());
        attemptsViewModel.setRoutineName(routineName);
        setTitle(routineName);

        RecyclerView recyclerView = binding.recyclerview;
        final RoutineScoreListAdapter adapter = new RoutineScoreListAdapter(this,
                attemptsViewModel, new RoutineScoreListAdapter.RoutineScoreDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        attemptsViewModel.getAllAttemptsForRoutine().observe(this, attempts -> {
            adapter.submitList(attempts);
        });

        attemptsViewModel.getSelectedDesc().observe(this, binding.multipleSelectText::setText);
        attemptsViewModel.getAbleToDelete().observe(this, binding.deleteSelectedButton::setEnabled);
    }

    /**
     * Show the result of the delete operation in a Snackbar popup.
     * @param view The button that triggered the action, which gives us a reference to the UI to
     *             attach the snackbar
     * @param success Whether the operation succeeded
     * @param numDeleted How many scores were deleted
     */
    private void showDeleteResult(View view, boolean success, int numDeleted) {
        int stringResId = R.string.success_message;
        if (!success) {
            stringResId = R.string.failure_try_again_message;
        }
        String msgToShow = getString(stringResId);
        if (success) {
            msgToShow = msgToShow + " " + numDeleted + " " + getString(R.string.deleted_label);
        }
        Snackbar snackbar = Snackbar.make(view, msgToShow, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}