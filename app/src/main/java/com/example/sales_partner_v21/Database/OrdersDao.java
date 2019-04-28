package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrdersDao {
    @Insert
    void InsertNewOrder(Orders order);

    @Query("SELECT MAX(id) FROM orders")
    int getMaxID();

    @Query("SELECT * FROM orders WHERE (REPLACE(date,'-','') BETWEEN :initialDate AND :finalDate) AND customer_id IN (:ids) AND status_id IN (:statuses);")
    List<Orders> getFilterOrders(String initialDate, String finalDate, int[] ids, int[] statuses);
}
