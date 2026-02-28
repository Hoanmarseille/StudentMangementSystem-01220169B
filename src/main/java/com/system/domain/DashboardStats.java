package com.system.domain;

public class DashboardStats {
    private final int totalStudents;
    private final double averageGpa;
    private final int activeStudents;

    public DashboardStats(int totalStudents, double averageGpa, int activeStudents) {
        this.totalStudents = totalStudents;
        this.averageGpa = averageGpa;
        this.activeStudents = activeStudents;
    }

    public int getTotalStudents() { return totalStudents; }
    public double getAverageGpa() { return averageGpa; }
    public int getActiveStudents() { return activeStudents; }
}