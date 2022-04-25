package com.huwdunnit.snookerimprover.ui.stats.allscores;

import com.huwdunnit.snookerimprover.model.RoutineScore;

/**
 * A callback for selected multiple routine scores.
 *
 * @author Huwdunnit
 */
public interface MultipleScoreSelectCallback {

    /**
     * Notify that the provided score has been clicked, receiving a state object in response, which
     * tells us whether the score should be in a "selected" or "deselected" state.
     * @param score The score that was clicked on
     * @return A state object, telling us whether the score is in a "selected" or "deselected" state
     */
    SelectedState scoreClicked(RoutineScore score);
}
