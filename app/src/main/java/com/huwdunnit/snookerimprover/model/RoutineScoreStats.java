package com.huwdunnit.snookerimprover.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Holds stats for a particular routine and time period.
 *
 * @author Huwdunnit
 */
public class RoutineScoreStats {

    /** Name of the routine stats have been requested for. */
    private final String routineName;

    /** Starting date for the stats request. If null, we're getting stats without time constraints */
    private final Date sinceDate;

    /** Number of attempts for the routine in the time period. */
    private final int numberOfAttempts;

    /** Best score for the routine in the time period. */
    private final RoutineScore bestScore;

    /** The average score for the routine in the time period. */
    private final double averageScore;

    public RoutineScoreStats(String routineName, Date sinceDate, int numberOfAttempts, RoutineScore bestScore, double averageScore) {
        this.routineName = routineName;
        this.sinceDate = sinceDate;
        this.numberOfAttempts = numberOfAttempts;
        this.bestScore = bestScore;
        BigDecimal bd = BigDecimal.valueOf(averageScore);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        this.averageScore = bd.doubleValue();
    }

    public String getRoutineName() {
        return routineName;
    }

    public Date getSinceDate() {
        return sinceDate;
    }

    public int getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public RoutineScore getBestScore() {
        return bestScore;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public static class RoutineScoreStatsBuilder {
        private String routineName;
        private Date sinceDate;
        private int numberOfAttempts;
        private RoutineScore bestScore;
        private double averageScore;

        public RoutineScoreStatsBuilder setRoutineName(String routineName) {
            this.routineName = routineName;
            return this;
        }

        public RoutineScoreStatsBuilder setSinceDate(Date sinceDate) {
            this.sinceDate = sinceDate;
            return this;
        }

        public RoutineScoreStatsBuilder setNumberOfAttempts(int numberOfAttempts) {
            this.numberOfAttempts = numberOfAttempts;
            return this;
        }

        public RoutineScoreStatsBuilder setBestScore(RoutineScore bestScore) {
            this.bestScore = bestScore;
            return this;
        }

        public RoutineScoreStatsBuilder setAverageScore(double averageScore) {
            this.averageScore = averageScore;
            return this;
        }

        public RoutineScoreStats createRoutineScoreStats() {
            return new RoutineScoreStats(routineName, sinceDate, numberOfAttempts, bestScore, averageScore);
        }
    }
}
