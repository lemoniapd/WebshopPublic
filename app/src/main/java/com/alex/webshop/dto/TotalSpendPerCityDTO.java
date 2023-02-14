package com.alex.webshop.dto;

import java.util.Objects;

public class TotalSpendPerCityDTO {
    protected final String name;
    protected final long amount;

    public TotalSpendPerCityDTO(String name, long amount) {
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
        TotalSpendPerCityDTO that = (TotalSpendPerCityDTO) o;
        return amount == that.amount && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, amount);
    }

    @Override
    public String toString() {
        return "TotalSpendPerCityDTO{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
