package com.snookerup.ui.stats;

import android.content.Context;
import android.util.Log;

import com.snookerup.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Models an option for viewing stats for a particular time period, includes the number of days to
 * count back from, and a label for the time period.
 *
 * @author Huwdunnit
 */
public class DaysToView implements Serializable {

    private static final String TAG = DaysToView.class.getName();

    private final int daysToView;

    private final String label;

    public DaysToView(int daysToView, String label) {
        this.daysToView = daysToView;
        this.label = label;
    }

    /**
     * Gets a date that corresponds to the current date, minus the number of days to view.
     * @return A Date object equalling the current date minus the selected days to view
     */
    public Date getDateMinusDaysFromNow() {
        if (daysToView <= 0) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -daysToView);
        return calendar.getTime();
    }

    /**
     * Construct a list of DaysToView objects, by parsing String array resources and creating
     * corresponding objects.
     * @param context The context, used to obtain String resources
     * @return A list of all possible days to view options. Could be null if there was an error
     * with String resources
     */
    public static List<DaysToView> constructListOfValues(Context context) {
        List<Integer> statsPeriodsDays = new ArrayList<>();
        List<String> statsPeriodLabels = new ArrayList<>();
        String[] statsPeriods = context.getResources().getStringArray(R.array.time_periods_numbers);
        for (String statsPeriod : statsPeriods) {
            statsPeriodsDays.add(Integer.parseInt(statsPeriod));
        }
        String[] statsPeriodsText = context.getResources().getStringArray(R.array.time_periods);
        for (String statsPeriodText : statsPeriodsText) {
            statsPeriodLabels.add(statsPeriodText);
        }
        if (statsPeriodsDays.size() != statsPeriodLabels.size()) {
            Log.e(TAG, "Error parsing days to view, don't have same number of days as labels");
            return null;
        }
        List<DaysToView> daysAndLabels = new ArrayList<>();
        for (int i = 0; i < statsPeriodLabels.size(); i++) {
            daysAndLabels.add(new DaysToView(statsPeriodsDays.get(i), statsPeriodLabels.get(i)));
        }
        Log.d(TAG, "Returning days to view options of " + daysAndLabels);
        return daysAndLabels;
    }

    public int getDaysToView() {
        return daysToView;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaysToView that = (DaysToView) o;
        return daysToView == that.daysToView && Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(daysToView, label);
    }

    @Override
    public String toString() {
        return "DaysToView{" +
                "daysToView=" + daysToView +
                ", label='" + label + '\'' +
                '}';
    }
}
