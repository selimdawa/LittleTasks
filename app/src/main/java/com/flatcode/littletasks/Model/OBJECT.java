package com.flatcode.littletasks.Model;

public class OBJECT {

    String id, name, publisher;
    int points;
    long timestamp;

    public OBJECT() {

    }

    public OBJECT(String id, String name, int points, String publisher, long timestamp) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.points = points;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
