package com.huwdunnit.snookerimprover.ui.info;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huwdunnit.snookerimprover.model.Routine;

public class InfoViewModel extends ViewModel {

    private final MutableLiveData<Integer> routineImageResId;

    private final MutableLiveData<Integer> routineFullScreenImageResId;

    private final MutableLiveData<String> routineDesc;

    public InfoViewModel() {
        routineImageResId = new MutableLiveData<>();
        routineFullScreenImageResId = new MutableLiveData<>();
        routineDesc = new MutableLiveData<>();
    }

    public LiveData<String> getRoutineDesc() {
        return routineDesc;
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
        String[] routineDescSteps = context.getResources().getStringArray(routine.getDescArrayResourceId());
        StringBuilder stepsBuilder = new StringBuilder();
        for (String step : routineDescSteps) {
            stepsBuilder.append(step);
            stepsBuilder.append("\n\n");
        }
        routineDesc.setValue(stepsBuilder.toString());
    }
}