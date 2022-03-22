package com.huwdunnit.snookerimprover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huwdunnit.snookerimprover.R;
import com.huwdunnit.snookerimprover.model.Routine;

import java.util.List;

/**
 * RecyclerView adapter for the list of available practice routines on the Home screen.
 *
 * @author Huwdunnit
 */
public class RoutineItemAdapter extends RecyclerView.Adapter<RoutineItemAdapter.ItemViewHolder>{

    private final Context context;

    private final List<Routine> routines;

    public RoutineItemAdapter(Context context, List<Routine> routines) {
        this.context = context;
        this.routines = routines;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.routine_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Routine routine = routines.get(position);
        holder.getTitleTextView().setText(context.getResources().getString(routine.getStringResourceId()));
        holder.getRoutineImageView().setImageResource(routine.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;

        private final ImageView routineImageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            routineImageView = itemView.findViewById(R.id.item_image);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        ImageView getRoutineImageView() {
            return routineImageView;
        }
    }
}
