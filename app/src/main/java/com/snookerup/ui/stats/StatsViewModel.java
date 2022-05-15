package com.snookerup.ui.stats;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.snookerup.data.scores.ScoreRepository;
import com.snookerup.model.Routine;
import com.snookerup.model.RoutineScore;
import com.snookerup.ui.common.ChangeableRoutineViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StatsViewModel extends ViewModel implements ChangeableRoutineViewModel {

    private static final String TAG = StatsViewModel.class.getName();

    private final MutableLiveData<String> numberOfAttempts;

    private final MutableLiveData<String> bestScore;

    private final MutableLiveData<String> averageScore;

    /**
     * If there are no scores for the routine at all, the button to view all attempts should be
     * disabled.
     */
    private final MutableLiveData<Boolean> haveAttemptsToView;

    private String unknownLabel;

    private String loadingLabel;

    private String noPreviousAttemptsLabel;

    private ScoreRepository scoreRepository;

    private String dateFormat;

    private volatile String routineName;

    private volatile Date sinceDate;

    private volatile DaysToView daysToView;

    /** A list of possible options for days to view that the user can pick. */
    private List<DaysToView> daysToViewOptions;

    public StatsViewModel() {
        super();
        numberOfAttempts = new MutableLiveData<>();
        bestScore = new MutableLiveData<>();
        averageScore = new MutableLiveData<>();
        haveAttemptsToView = new MutableLiveData<>();
        haveAttemptsToView.setValue(false);
    }

    public LiveData<String> getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public LiveData<String> getBestScore() {
        return bestScore;
    }

    public LiveData<String> getAverageScore() {
        return averageScore;
    }

    public LiveData<Boolean> getHaveAttemptsToView() {
        return haveAttemptsToView;
    }

    public void setUnknownLabel(String unknownLabel) {
        this.unknownLabel = unknownLabel;
    }

    public void setLoadingLabel(String loadingLabel) {
        this.loadingLabel = loadingLabel;
    }

    public void setNoPreviousAttemptsLabel(String noPreviousAttemptsLabel) {
        this.noPreviousAttemptsLabel = noPreviousAttemptsLabel;
    }

    public void setScoreRepository(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public void setRoutine(Routine routine, Context context) {
        synchronized (this) {
            this.routineName = routine.getName();
        }
        //Routine changed, so check if we have any scores ever for this routine
        scoreRepository.haveAnyScoresForRoutine(routineName, haveRoutines -> {
            this.haveAttemptsToView.postValue(haveRoutines);
        }, () -> {
            //Unable to check DB, so just disable this button
            this.haveAttemptsToView.postValue(false);
        });
        reloadStats();
    }

    public void setDaysToViewOptions(List<DaysToView> daysToViewOptions) {
        this.daysToViewOptions = daysToViewOptions;
    }

    /**
     * Set the index for the days to view selection. Note that this is the list index, not the
     * actual value.
     * @param index The list index for the days to view election
     */
    public void setDaysToViewIndex(int index) {
        this.daysToView = daysToViewOptions.get(index);
        this.sinceDate = daysToView.getDateMinusDaysFromNow();
        reloadStats();
    }

    /**
     * Get the currently selected days to view option.
     * @return The selected days to view
     */
    public DaysToView getDaysToView() {
        return daysToView;
    }

    /**
     * Reload stats from the DB, based on the currently set routine name and date.
     */
    public void reloadStats() {
        Log.d(TAG, "reloadStats, first set loading labels on all fields");
        this.numberOfAttempts.setValue(loadingLabel);
        this.bestScore.setValue(loadingLabel);
        this.averageScore.setValue(loadingLabel);

        Log.d(TAG, "Making DB call to get stats");
        scoreRepository.getStatsForRoutine(routineName, sinceDate, (stats) -> {
            //Success callback
            if (validateCorrectResult(stats.getRoutineName(), stats.getSinceDate())) {
                this.numberOfAttempts.postValue(String.valueOf(stats.getNumberOfAttempts()));
                RoutineScore bestScore = stats.getBestScore();
                if (bestScore == null) {
                    this.bestScore.postValue(noPreviousAttemptsLabel);
                } else {
                    SimpleDateFormat dateFormatObj = new SimpleDateFormat(dateFormat);
                    String date = dateFormatObj.format(bestScore.getDateTime());
                    this.bestScore.postValue(bestScore.getScore() + " (" + date + ")");
                }
                this.averageScore.postValue(String.valueOf(stats.getAverageScore()));
            } else {
                Log.d(TAG, "Results are not for the most recently set routine, wanted routineName="
                        + routineName + " and sinceDate=" + sinceDate + ", got routineName="
                        + stats.getRoutineName() + " and sinceDate=" + stats.getSinceDate());
            }
        }, () -> {
            //Error callback
            Log.e(TAG, "Error getting values from DB, setting all to Unknown");
            this.numberOfAttempts.postValue(unknownLabel);
            this.bestScore.postValue(unknownLabel);
            this.averageScore.postValue(unknownLabel);
        });
    }

    /**
     * Ensures that the stats returned in a callback are for the currently set routine and date
     * (in case the user changes values very quickly).
     * @param routineName The name of the routine returned in the stats callback
     * @param sinceDate The date returned in the stats callback
     * @return True if the provided values match the member variables on the class, false otherwise
     */
    private boolean validateCorrectResult(String routineName, Date sinceDate) {
        synchronized (this) {
            return Objects.equals(this.routineName, routineName) && Objects.equals(this.sinceDate, sinceDate);
        }
    }
}