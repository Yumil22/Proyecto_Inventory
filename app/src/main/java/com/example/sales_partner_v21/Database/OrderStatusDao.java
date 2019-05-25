package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrderStatusDao {
    @Query("SELECT description FROM order_status")
    List<String> getAllOrderStatusDescription();

    @Query("SELECT * FROM order_status WHERE id = :order_id")
    OrderStatus getOrderStatusByID(int order_id);

    @Query("SELECT id FROM order_status")
    List<Integer> getAllOrdersIDs();

    @Query("Delete from order_status")
    public void DeleteOrderStatusTable();

    @Insert
    void InsertOrderStatus(OrderStatus orderStatus);
}
