package com.huwdunnit.snookerimprover.ui.stats.allscores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.model.DateAndTime;
import com.huwdunnit.snookerimprover.model.RoutineScore;

import java.util.Date;

/**
 * A list adapter for displaying all scores for a routine in the {@link RoutineScoresListActivity}
 * class.
 *
 * @author Huwdunnit
 */
public class RoutineScoreListAdapter extends ListAdapter<RoutineScore, RoutineScoreListAdapter.RoutineScoreViewHolder> {

    /** The Drawable resource to use as the background when an item is selected. */
    private static final int SELECTED_BG = R.drawable.rounded_corner_bg_highlighted;

    /** The Drawable resource to use as the background when an item is not selected. */
    private static final int DESELECTED_BG = R.drawable.rounded_corner_bg;

    private final Context context;

    /** A callback to notify when an item is clicked. */
    private final MultipleScoreSelectCallback selectCallback;

    public RoutineScoreListAdapter(Context context, MultipleScoreSelectCallback selectCallback,
                                   @NonNull DiffUtil.ItemCallback<RoutineScore> diffCallback) {
        super(diffCallback);
        this.context = context;
        this.selectCallback = selectCallback;
    }

    @Override
    public RoutineScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RoutineScoreListAdapter.RoutineScoreViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RoutineScoreViewHolder holder, int position) {
        RoutineScore current = getItem(position);
        holder.getScoreTextView().setText(current.getScoreString());
        Date scoreDate = current.getDateTime();
        DateAndTime dateAndTime = DateAndTime.fromDate(scoreDate, context.getString(R.string.date_format));
        holder.getDateTextView().setText(dateAndTime.getDate());
        holder.getTimeTextView().setText(dateAndTime.getTime());
        holder.getRootView().setOnClickListener(view -> {
            SelectedState state = selectCallback.scoreClicked(current);
            if (state == SelectedState.SELECTED) {
                view.setBackgroundResource(SELECTED_BG);
            } else {
                view.setBackgroundResource(DESELECTED_BG);
            }
        });
    }

    static class RoutineScoreDiff extends DiffUtil.ItemCallback<RoutineScore> {

        @Override
        public boolean areItemsTheSame(@NonNull RoutineScore oldItem, @NonNull RoutineScore newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull RoutineScore oldItem, @NonNull RoutineScore newItem) {
            return oldItem.getScoreString().equals(newItem.getScoreString());
        }
    }

    protected class RoutineScoreViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;

        private final TextView scoreTextView;

        private final TextView dateTextView;

        private final TextView timeTextView;

        public RoutineScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.getRootView();
            scoreTextView = itemView.findViewById(R.id.score_text);
            dateTextView = itemView.findViewById(R.id.date_text);
            timeTextView = itemView.findViewById(R.id.time_text);
        }

        View getRootView() {
            return rootView;
        }

        TextView getScoreTextView() {
            return scoreTextView;
        }

        TextView getDateTextView() {
            return dateTextView;
        }

        TextView getTimeTextView() {
            return timeTextView;
        }
    }
}
