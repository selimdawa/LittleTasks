package com.flatcode.littletasks.Model;

public class Category {

    String id, name, image, publisher, plan;
    long timestamp;

    public Category() {

    }

    public Category(String id, String name, String image, String publisher, String plan, long timestamp) {
        this.id = id;
        this.name = name;
        this.publisher = publisher;
        this.image = image;
        this.plan = plan;
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

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
