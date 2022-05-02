package com.snookerup.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.snookerup.model.Routine;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Routine>> routines;

    public HomeViewModel() {
        routines = new MutableLiveData<>();
    }

    public void setRoutines(List<Routine> routines) {
        this.routines.setValue(routines);
    }

    public LiveData<List<Routine>> getRoutines() {
        return routines;
    }
}