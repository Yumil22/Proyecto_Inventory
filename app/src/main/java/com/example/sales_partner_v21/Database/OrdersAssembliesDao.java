package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrdersAssembliesDao {
    @Insert
    void InserNewOrdersAssembly(OrderAssemblies orderAssembly);

    @Query("SELECT SUM(qty) FROM order_assemblies WHERE order_id = :id")
    int getQtyAssemblies(int id);


    @Query("SELECT * FROM order_assemblies")
    public List<OrderAssemblies> getAllorderAssemblies();
}
