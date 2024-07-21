package com.example.dashboard_test;

import java.sql.Time;
public class Schedule {
    private int scheduleId;
    private int userId;
    private String courseSection;
    private String subjectCode;
    private String subject;
    private String date;
    private Time startTime;
    private Time endTime;

    public Schedule(int scheduleId, int userId, String courseSection, String subjectCode, String subject, String date, Time startTime, Time endTime) {
        this.scheduleId = scheduleId;
        this.userId = userId;
        this.courseSection = courseSection;
        this.subjectCode = subjectCode;
        this.subject = subject;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public int getUserId() {
        return userId;
    }

    public String getCourseSection() {
        return courseSection;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public String getTimePeriod() {
        return startTime + " - " + endTime;
    }
}
