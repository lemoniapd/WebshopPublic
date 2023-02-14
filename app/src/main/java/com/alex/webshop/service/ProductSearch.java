package com.alex.webshop.service;

import com.alex.webshop.model.Order;

@FunctionalInterface
public interface ProductSearch {
    boolean exists(int orderId, Order order);
}
