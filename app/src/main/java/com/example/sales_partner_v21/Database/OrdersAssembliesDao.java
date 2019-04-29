package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface OrdersAssembliesDao {
    @Insert
    void InserNewOrdersAssembly(OrderAssemblies orderAssembly);

    @Query("SELECT SUM(qty) FROM order_assemblies WHERE order_id = :id")
    int getQtyAssemblies(int id);

    // SUPER QUERY
    @Query("SELECT SUM(order_assemblies.qty * products.price) FROM order_assemblies INNER JOIN assembly_products INNER JOIN products ON assembly_products.product_id = products.id AND order_assemblies.assembly_id = assembly_products.id WHERE order_assemblies.order_id = :id")
    int getTotalCostOrdersAssemblies(int id);
}
