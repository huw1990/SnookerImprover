package com.huwdunnit.snookerimprover.ui.stats.graph;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.huwdunnit.snookerimprover.R;

/**
 * A custom marker view, extending the default view provided by the third party MPAndroidChart
 * library, providing specifics for a score through a callback.
 *
 * @author Huwdunnit
 */
public class ScoreDetailMarkerView extends MarkerView {

    /** A callback to query for a label for the entry. */
    private final EntrySelectedCallback callback;

    /** The text view to carry the label for the entry. */
    private TextView labelTextView;

    private MPPointF mOffset;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context App context
     * @param layoutResource Layout resource file ID for the custom view
     * @param callback The callback for getting a label for the entry
     */
    public ScoreDetailMarkerView(Context context, int layoutResource, EntrySelectedCallback callback) {
        super(context, layoutResource);
        this.callback = callback;

        labelTextView = findViewById(R.id.label_content);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        //Get a label for the entry from the callback
        labelTextView.setText(callback.getLabelForEntry(e));

        //Update the layout
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }

}
