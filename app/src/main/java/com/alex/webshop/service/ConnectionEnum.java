package com.alex.webshop.service;

public enum ConnectionEnum {
    //blanked out due to security reasons
    ALEX("jdbc:mysql://10.0.2.2:3306/Webshop?allowPublicKeyRetrieval=true&useSSL=false", "XXX", "XXX"),
    LEMONIA("jdbc:mysql://10.0.2.2:3306/Webshop?allowPublicKeyRetrieval=true&useSSL=false", "XXX", "XXX-"),
    PRODUCTION("jdbc:mysql://mysql-444cb83-nackademin-android.aivencloud.com:11923/Webshop?sslmode=require", "XXX", "XXX");

    private final String connString;
    private final String user;
    private final String pass;

    ConnectionEnum(String conn, String user, String pass) {
        this.connString = conn;
        this.user = user;
        this.pass = pass;
    }

    public String getConnString() {
        return connString;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
