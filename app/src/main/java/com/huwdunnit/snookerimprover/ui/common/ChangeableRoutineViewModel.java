package com.huwdunnit.snookerimprover.ui.common;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.huwdunnit.snookerimprover.model.Routine;

/**
 * A view model to handle the common UI components needed to manage the changing of routines, e.g.
 * the routine image and the spinner.
 *
 * @author Huwdunnit
 */
public abstract class ChangeableRoutineViewModel extends ViewModel {

    /**
     * Handle the underlying routine being changed.
     * @param routine The new routine that's been selected
     * @param context The context
     */
    public abstract void setRoutine(Routine routine, Context context);
}
