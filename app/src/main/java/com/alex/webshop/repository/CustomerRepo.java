package com.alex.webshop.repository;

import android.util.Log;
import com.alex.webshop.model.Customer;
import com.alex.webshop.model.Product;
import com.alex.webshop.service.ConnectionEnum;
import com.alex.webshop.service.Response;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    private final ConnectionEnum connectionEnum;

    public CustomerRepo(ConnectionEnum connectionEnum) {
        this.connectionEnum = connectionEnum;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        Customer tempCustomer;

        try (Connection con = DriverManager.getConnection(connectionEnum.getConnString(), connectionEnum.getUser(), connectionEnum.getPass())) {

            // The object used for executing a static SQL statement and returning the results it produces
            Statement statement = con.createStatement();

            // A table of data representing a database result set,
            // which is usually generated by executing a statement that queries the database.
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer");

            // Displaying results
            while (resultSet.next()) {
                tempCustomer = new Customer(); // skapas ny customer här

                tempCustomer.setId(resultSet.getInt("id"));
                tempCustomer.setFirstName(resultSet.getString("firstName"));
                tempCustomer.setLastName(resultSet.getString("lastName"));
                tempCustomer.setPassword(resultSet.getString("pwd"));
                tempCustomer.setEmail(resultSet.getString("email"));
                tempCustomer.setAddress(resultSet.getString("address"));
                tempCustomer.setCity(resultSet.getString("city"));
                tempCustomer.setZipCode(resultSet.getString("zipCode"));
                allCustomers.add(tempCustomer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("webshopen", "Error reading customers from db");
        }
        return allCustomers;
    }

    public Response addToCart(Customer customer, int orderId, Product product) {
        try (Connection con = DriverManager.getConnection(connectionEnum.getConnString(), connectionEnum.getUser(), connectionEnum.getPass())) {
            CallableStatement callableStatement = con.prepareCall("CALL addToCart(?, ?, ?)");
            callableStatement.setInt(1, customer.getId());
            callableStatement.setInt(2, orderId);
            callableStatement.setInt(3, product.getProductSizeId());
            callableStatement.executeQuery();
            Log.d("webshopen", "Customer: " + customer.getId() + " Order: " + orderId + " Product: " + product.getProductSizeId());
            return new Response("Product Ordered", true);
        } catch (SQLException e) {
            Log.d("webshopen", e.getMessage());
            return new Response(e.getMessage(), false);
        }
    }
}
