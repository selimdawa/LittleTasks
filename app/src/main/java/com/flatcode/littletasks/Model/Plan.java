package com.flatcode.littletasks.Model;

public class Plan {

    String id, name, image, publisher;
    long timestamp;

    public Plan() {

    }

    public Plan(String id, String name, String image, String publisher, long timestamp) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
