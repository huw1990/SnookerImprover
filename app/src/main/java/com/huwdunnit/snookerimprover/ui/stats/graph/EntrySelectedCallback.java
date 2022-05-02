package com.huwdunnit.snookerimprover.ui.stats.graph;

import com.github.mikephil.charting.data.Entry;

/**
 * A callback for when a graph entry has been selected.
 *
 * @author Huwdunnit
 */
public interface EntrySelectedCallback {

    /**
     * Create and return an appropriate label for the provided selected chart entry.
     * @param entry The chart entry
     * @return A label to display for the entry when it's selected
     */
    String getLabelForEntry(Entry entry);
}
