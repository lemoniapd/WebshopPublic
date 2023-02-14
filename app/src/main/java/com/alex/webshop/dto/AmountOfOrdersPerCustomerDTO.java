package com.alex.webshop.dto;

import com.alex.webshop.model.Customer;

import java.util.Objects;

public class AmountOfOrdersPerCustomerDTO {
    protected final Customer customer;
    protected final long amount;

    public AmountOfOrdersPerCustomerDTO(Customer customer, long amount) {
        this.customer = customer;
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmountOfOrdersPerCustomerDTO that = (AmountOfOrdersPerCustomerDTO) o;
        return amount == that.amount && customer.equals(that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, amount);
    }

    @Override
    public String toString() {
        return "AmountOfOrdersPerCustomerDTO{" +
                "customer=" + customer +
                ", amount=" + amount +
                '}';
    }
}
