package com.alex.webshop.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private int id;
    private String name;
    private int price;
    private Brand brand;
    private int stock;
    private int euSize;
    private Color color;
    private List<Category> category;
    private int productSizeId;

    public Product(int id, String name, int price, Brand brand, int stock, int euSize, Color color, List<Category> category, int productSizeId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.stock = stock;
        this.euSize = euSize;
        this.color = color;
        this.category = category;
        this.productSizeId = productSizeId;
    }

    public Product(String name, int price, Brand brand, int stock, int euSize, Color color, List<Category> category, int productSizeId) {
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.stock = stock;
        this.euSize = euSize;
        this.color = color;
        this.category = category;
        this.productSizeId = productSizeId;
    }

    public Product() {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getEuSize() {
        return euSize;
    }

    public void setEuSize(int euSize) {
        this.euSize = euSize;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public int getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(int productSizeId) {
        this.productSizeId = productSizeId;
    }

    public String getProductDescription() {
        return getBrand().getName() + "\n" +
                getName() + "\n" +
                getColor().getName() + "\n" +
                "EU-size: " + getEuSize() + "\n" +
                "Stock: " + getStock() + "pcs\n" +
                "Price: " + getPrice() + " SEK";
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", brand=" + brand +
                ", stock=" + stock +
                ", euSize=" + euSize +
                ", color=" + color +
                ", category=" + category +
                ", productSizeId=" + productSizeId +
                '}';
    }
}
