package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrderStatusChangesDao {
    @Insert
    void InsertOrderStatusChanged(OrderStatusChanges orderStatusChanges);

    @Query("SELECT * FROM order_status_changes WHERE order_id = :orderID ORDER BY id DESC")
    List<OrderStatusChanges> getOrderStatusChangesByOrderID(int orderID);
}
