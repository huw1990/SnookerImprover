package com.snookerup.ui.info;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.snookerup.model.Routine;
import com.snookerup.ui.common.ChangeableRoutineViewModel;

public class InfoViewModel extends ViewModel implements ChangeableRoutineViewModel{

    private final MutableLiveData<Drawable> routineImage;

    private final MutableLiveData<String> routineFullScreenImageFilename;

    private final MutableLiveData<String> routineDesc;

    public InfoViewModel() {
        super();
        routineImage = new MutableLiveData<>();
        routineFullScreenImageFilename = new MutableLiveData<>();
        routineDesc = new MutableLiveData<>();
    }

    public LiveData<Drawable> getRoutineImage() {
        return routineImage;
    }

    public LiveData<String> getRoutineFullScreenImageFilename() {
        return routineFullScreenImageFilename;
    }

    public LiveData<String> getRoutineDesc() {
        return routineDesc;
    }

    @Override
    public void setRoutine(Routine routine, Context context) {
        this.routineImage.postValue(routine.getLandscapeImage(context.getAssets()));
        routineFullScreenImageFilename.postValue(routine.getPortraitImageFileName());
        StringBuilder stepsBuilder = new StringBuilder();
        for (String step : routine.getDescriptionLines()) {
            stepsBuilder.append(step);
            stepsBuilder.append("\n\n");
        }
        routineDesc.postValue(stepsBuilder.toString());
    }
}