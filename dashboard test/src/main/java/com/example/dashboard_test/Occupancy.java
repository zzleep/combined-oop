package com.example.dashboard_test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Occupancy {
    private SimpleIntegerProperty occupancyId;
    private SimpleStringProperty room;
    private SimpleStringProperty professor;
    private SimpleStringProperty courseSection;
    private SimpleStringProperty subject;
    private SimpleStringProperty time;
    private SimpleStringProperty status;

    public Occupancy(int occupancyId, String room, String professor, String courseSection, String subject, String time, String status) {
        this.occupancyId = new SimpleIntegerProperty(occupancyId);
        this.room = new SimpleStringProperty(room);
        this.professor = new SimpleStringProperty(professor);
        this.courseSection = new SimpleStringProperty(courseSection);
        this.subject = new SimpleStringProperty(subject);
        this.time = new SimpleStringProperty(time);
        this.status = new SimpleStringProperty(status);
    }

    // Getters
    public int getOccupancyId() {
        return occupancyId.get();
    }

    public String getRoom() {
        return room.get();
    }

    public String getProfessor() {
        return professor.get();
    }

    public String getCourseSection() {
        return courseSection.get();
    }

    public String getSubject() {
        return subject.get();
    }

    public String getTime() {
        return time.get();
    }

    public String getStatus() {
        return status.get();
    }

    public boolean matchesSearch(String searchText) {
        if (searchText == null || searchText.isEmpty()) return true;

        return (room.get() != null && room.get().toLowerCase().contains(searchText)) ||
                (professor.get() != null && professor.get().toLowerCase().contains(searchText)) ||
                (courseSection.get() != null && courseSection.get().toLowerCase().contains(searchText)) ||
                (subject.get() != null && subject.get().toLowerCase().contains(searchText)) ||
                (time.get() != null && time.get().toLowerCase().contains(searchText)) ||
                (status.get() != null && status.get().toLowerCase().contains(searchText));
    }
}
