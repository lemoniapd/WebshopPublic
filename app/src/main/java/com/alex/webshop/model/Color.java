package com.alex.webshop.model;

import java.io.Serializable;

public class Color implements Serializable {
    private int id;
    private String name;

    public Color(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Color(String name) {
        this.name = name;
    }

    public Color() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Color{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
