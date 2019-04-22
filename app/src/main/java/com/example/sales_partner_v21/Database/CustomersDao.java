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
}
