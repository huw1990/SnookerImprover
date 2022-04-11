package com.huwdunnit.snookerimprover.ui.info;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.huwdunnit.snookerimprover.model.Routine;
import com.huwdunnit.snookerimprover.ui.common.ChangeableRoutineViewModel;

public class InfoViewModel extends ChangeableRoutineViewModel {

    private final MutableLiveData<String> routineDesc;

    public InfoViewModel() {
        super();
        routineDesc = new MutableLiveData<>();
    }

    public LiveData<String> getRoutineDesc() {
        return routineDesc;
    }

    @Override
    public void setRoutine(Routine routine, Context context) {
        super.setRoutine(routine, context);
        String[] routineDescSteps = context.getResources().getStringArray(routine.getDescArrayResourceId());
        StringBuilder stepsBuilder = new StringBuilder();
        for (String step : routineDescSteps) {
            stepsBuilder.append(step);
            stepsBuilder.append("\n\n");
        }
        routineDesc.setValue(stepsBuilder.toString());
    }
}