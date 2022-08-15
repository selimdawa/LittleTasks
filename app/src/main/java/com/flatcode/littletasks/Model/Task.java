package com.flatcode.littletasks.Model;

public class Task {

    String name, publisher, id, category;
    long timestamp, start, end;
    int points, AVPoints;

    public Task() {

    }

    public Task(String name, String publisher, String id, String category,
                long timestamp, long start, long end, int points, int AVPoints) {
        this.name = name;
        this.publisher = publisher;
        this.id = id;
        this.category = category;
        this.timestamp = timestamp;
        this.start = start;
        this.end = end;
        this.points = points;
        this.AVPoints = AVPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAVPoints() {
        return AVPoints;
    }

    public void setAVPoints(int AVPoints) {
        this.AVPoints = AVPoints;
    }
}