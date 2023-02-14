package com.alex.webshop.service;
/*
    Används I AddToCart i CustomerRepo
    Som response objekt med boolean samt medd/felmedd
 */

public class Response {
    private final String message;
    private final boolean isSuccessful;

    public Response(String message, boolean isSuccessful) {
        this.message = message;
        this.isSuccessful = isSuccessful;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }
}
