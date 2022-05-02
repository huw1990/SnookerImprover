package com.snookerup.ui.stats.graph;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.snookerup.R;
import com.snookerup.data.ScoreRepository;
import com.snookerup.databinding.ActivityStatsGraphBinding;
import com.snookerup.model.DateAndTime;
import com.snookerup.model.RoutineScore;
import com.snookerup.ui.stats.DaysToView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An activity that displays a graph showing the scores for a particular routine over a particular
 * time period.
 *
 * @author Huwdunnit
 */
public class StatsGraphActivity extends AppCompatActivity implements EntrySelectedCallback {

    public static final String ROUTINE_NAME = "com.snookerup.ROUTINE_NAME";

    public static final String DAYS_TO_VIEW = "com.snookerup.DAYS_TO_VIEW";

    private static final String TAG = StatsGraphActivity.class.getName();

    private ActivityStatsGraphBinding binding;

    private BarChart chart;

    private volatile List<RoutineScore> scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStatsGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chart = binding.chart;

        //Animate the drawing of the graph
        chart.animateXY(1000, 1000);

        //Set a custom marker, so we can display the date/time and score of each bar on the chart
        chart.setMarker(new ScoreDetailMarkerView(this, R.layout.graph_label_marker_view, this));

        //Configure the look and feel of the chart, axis, and legend
        chart.getDescription().setEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        //Get the values about the chart we want to draw from the intent
        Intent intent = getIntent();
        String routineName = intent.getStringExtra(ROUTINE_NAME);
        DaysToView daysToView = (DaysToView) intent.getSerializableExtra(DAYS_TO_VIEW);
        Date sinceDate = daysToView.getDateMinusDaysFromNow();
        setTitle(routineName + " - " + daysToView.getLabel());

        //Load the scores from the DB, but wait for them to be populated in LiveData
        LiveData<List<RoutineScore>> scoresForRoutine = new ScoreRepository(this).getAllScoresForRoutine(routineName, sinceDate);
        scoresForRoutine.observe(this, scores -> {
            Log.d(TAG, "Scores populated from LiveData, setting on BarChart now");
            setScores(scores);

            //Convert the RoutineScore objects to BarEntry objects
            List<BarEntry> entries = new ArrayList<>();
            for (int i = 0; i < scores.size(); i++) {
                entries.add(new BarEntry(i + 1, scores.get(i).getScore()));
            }

            BarDataSet set1 = new BarDataSet(entries, "Scores");
            set1.setColor(getColor(R.color.red_dark));
            BarData data = new BarData(set1);

            //Set the data then refresh the chart
            chart.setData(data);
            chart.invalidate();
        });
    }

    private void setScores(List<RoutineScore> scores) {
        this.scores = scores;
    }

    @Override
    public String getLabelForEntry(Entry entry) {
        RoutineScore scoreForEntry = scores.get(((int) entry.getX()) - 1);
        DateAndTime dateAndTime = DateAndTime.fromDate(scoreForEntry.getDateTime(), getString(R.string.date_format));
        return dateAndTime.getDate() + ", " + dateAndTime.getTime() + " - " + scoreForEntry.getScore();
    }
}