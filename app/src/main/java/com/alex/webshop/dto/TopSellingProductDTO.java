package com.alex.webshop.dto;

import java.util.Objects;

public class TopSellingProductDTO {
    protected final String name;
    protected final long amount;

    public TopSellingProductDTO(String name, long amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopSellingProductDTO that = (TopSellingProductDTO) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "TopSellingProductDTO{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
