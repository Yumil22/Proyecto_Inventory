package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrderStatusDao {
    @Query("SELECT description FROM order_status")
    List<String> getAllOrderStatusDescription();

}
