package com.huwdunnit.snookerimprover.ui.common;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huwdunnit.snookerimprover.model.Routine;

/**
 * A view model to handle the common UI components needed to manage the changing of routines, e.g.
 * the routine image and the spinner.
 *
 * @author Huwdunnit
 */
public class ChangeableRoutineViewModel extends ViewModel {

    private final MutableLiveData<Integer> routineImageResId;

    private final MutableLiveData<Integer> routineFullScreenImageResId;

    public ChangeableRoutineViewModel() {
        routineImageResId = new MutableLiveData<>();
        routineFullScreenImageResId = new MutableLiveData<>();
    }

    public LiveData<Integer> getRoutineImageResId() {
        return routineImageResId;
    }

    public LiveData<Integer> getRoutineFullScreenImageResId() {
        return routineFullScreenImageResId;
    }

    public void setRoutine(Routine routine, Context context) {
        routineImageResId.setValue(routine.getImageResourceId());
        routineFullScreenImageResId.setValue(routine.getFullScreenImageResourceId());
    }
}
