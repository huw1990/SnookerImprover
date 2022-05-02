package com.snookerup.ui.stats.allscores;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.snookerup.data.ScoreRepository;
import com.snookerup.model.RoutineScore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * View model for the {@link RoutineScoresListActivity} activity.
 *
 * @author Huwdunnit
 */
public class RoutineScoresViewModel extends AndroidViewModel implements MultipleScoreSelectCallback {

    /** LiveData to track whether there is anything to delete, i.e. any rows selected. */
    private final MutableLiveData<Boolean> ableToDelete = new MutableLiveData<>();

    /**
     * LiveData to track whether we should display help text, or an indication of the number of rows
     * that are selected.
     */
    private final MutableLiveData<String> selectedDesc = new MutableLiveData<>();

    /** All the scores that are currently selected by the user. */
    private final Set<RoutineScore> selectedScores = new HashSet<>();

    private ScoreRepository scoreRepository;

    private LiveData<List<RoutineScore>> allAttempts;

    private String noneSelectedString;

    private String selectedString;

    public Set<RoutineScore> getScoresToDelete() {
        return selectedScores;
    }

    public RoutineScoresViewModel(Application application) {
        super(application);
        scoreRepository = new ScoreRepository(application);
    }

    public void clearSelectedScores() {
        this.selectedScores.clear();
        updateSelectedDesc();
    }

    public void setNoneSelectedString(String noneSelectedString) {
        this.noneSelectedString = noneSelectedString;
    }

    public void setSelectedString(String selectedString) {
        this.selectedString = selectedString;
    }

    public void setRoutineName(String routineName) {
        allAttempts = scoreRepository.getAllScoresForRoutine(routineName, null);
    }

    LiveData<List<RoutineScore>> getAllAttemptsForRoutine() {
        return allAttempts;
    }

    LiveData<String> getSelectedDesc() {
        return selectedDesc;
    }

    LiveData<Boolean> getAbleToDelete() {
        return ableToDelete;
    }

    @Override
    public SelectedState scoreClicked(RoutineScore score) {
        SelectedState returnVal;
        if (selectedScores.contains(score)) {
            selectedScores.remove(score);
            returnVal = SelectedState.DESELECTED;
        } else {
            selectedScores.add(score);
            returnVal = SelectedState.SELECTED;
        }
        updateSelectedDesc();
        return returnVal;
    }

    private void updateSelectedDesc() {
        if (selectedScores.isEmpty()) {
            selectedDesc.postValue(noneSelectedString);
            ableToDelete.postValue(false);
        } else {
            selectedDesc.postValue(selectedScores.size() + " " + selectedString);
            ableToDelete.postValue(true);
        }
    }
}