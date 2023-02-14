package com.alex.webshop.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Order implements Serializable {
    private int id;
    private Customer customer;
    private List<Product> listOfProducts;

    public Order(int id, Customer customer, List<Product> listOfProducts) {
        this.id = id;
        this.customer = customer;
        this.listOfProducts = listOfProducts;
    }

    public Order(Customer customer, List<Product> listOfProducts) {
        this.customer = customer;
        this.listOfProducts = listOfProducts;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public int getTotalOrderPrice() {
        return listOfProducts.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }



    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer=" + customer +
                ", contains=" + listOfProducts +
                '}';
    }

    // eftersom det är objekt så måste vi ha dessa för att särskilja objekten med stream api distinct()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
