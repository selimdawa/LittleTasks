package com.flatcode.littletasks.Model;

public class Setting {

    String id, name, type;
    int image, number;
    Class c;

    public Setting() {

    }

    public Setting(String id, String name, int image, int number, Class c) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.number = number;
        this.c = c;
    }

    public Setting(String id, String name, int image, int number, String type) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.number = number;
        this.type = type;
    }

    public Setting(String id, String name, int image, int number) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.number = number;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Class getC() {
        return c;
    }

    public void setC(Class c) {
        this.c = c;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}