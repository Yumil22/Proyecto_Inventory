package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CustomersDao {

    @Query("SELECT * FROM CUSTOMERS ORDER BY id")
    public List<Customers> getAllCustomers();

}
