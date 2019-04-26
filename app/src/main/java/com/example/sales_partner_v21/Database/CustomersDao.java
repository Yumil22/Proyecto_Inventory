package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CustomersDao {

    @Query("SELECT * FROM CUSTOMERS ORDER BY id")
     List<Customers> getAllCustomers();

    @Query("SELECT * FROM CUSTOMERS WHERE ID = :id")
    Customers getCustomerById(int id);

    @Query("SELECT last_name FROM CUSTOMERS")
    List<String> getAllCustomerLastNames();

    @Query("SELECT * FROM CUSTOMERS WHERE first_name LIKE :name")
    List<Customers> getCustomersbyFirstname (String name);

    @Query("SELECT * FROM CUSTOMERS WHERE last_name LIKE :last_name")
    List<Customers> getCustomersbyLastname (String last_name);

    @Query("SELECT * FROM CUSTOMERS WHERE address LIKE :address")
    List<Customers> getCustomersbyAddress (String address);

    @Query("SELECT * FROM CUSTOMERS WHERE email LIKE :email")
    List<Customers> getCustomersbyEmail (String email);

    @Query("SELECT * FROM CUSTOMERS WHERE phone1 LIKE :phone OR phone2 LIKE :phone OR phone3 LIKE :phone")
    List<Customers> getCustomersbyPhone (String phone);

    @Query("SELECT id FROM CUSTOMERS ORDER BY id DESC LIMIT 1")
    int getMaxId();
}
