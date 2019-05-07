package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrdersAssembliesDao {
    @Insert
    void InsertNewOrdersAssembly(OrderAssemblies orderAssembly);

    @Query("SELECT SUM(qty) FROM order_assemblies WHERE order_id = :id")
    int getQtyAssemblies(int id);

    // SUPER QUERY
    @Query("SELECT SUM(order_assemblies.qty * products.price) FROM order_assemblies INNER JOIN assembly_products INNER JOIN products ON (assembly_products.product_id = products.id) AND (order_assemblies.assembly_id = assembly_products.assembly_id) WHERE order_assemblies.order_id = :id")
    int getTotalCostOrdersAssemblies(int id);

    @Query("SELECT * FROM order_assemblies")
    public List<OrderAssemblies> getAllorderAssemblies();


    @Query("SELECT oa.id as id, oa.order_id , oa.assembly_id, oa.qty FROM orders o " +
            "INNER JOIN order_assemblies oa ON o.id = oa.order_id " +
            "INNER JOIN Assemblies a ON a.id = oa.assembly_id  " +
            "WHERE date >= :date AND date <= :date2  ORDER BY oa.order_id")
    public List<OrderAssemblies> getordersAssembliesbyDate(String date, String date2);

    @Query( "SELECT a.description FROM orders o " +
            "INNER JOIN order_assemblies oa ON o.id = oa.order_id " +
            "INNER JOIN Assemblies a ON a.id = oa.assembly_id " +
            "WHERE date >= :date AND date <= :date2   ORDER BY oa.order_id")
    public List<String> getDescriptionbyDate(String date, String date2);


    @Query("SELECT assemblies.* FROM order_assemblies INNER JOIN assemblies ON order_assemblies.assembly_id = assemblies.id WHERE order_assemblies.order_id = :id")
    List<Assemblies> getAllAssembliesByOrderID(int id);

    @Query("SELECT qty FROM order_assemblies WHERE order_id = :id")
    List<Integer> getQtyAssembliesByOrderID(int id);

    @Query("SELECT SUM(order_assemblies.qty * products.price) FROM order_assemblies INNER JOIN assembly_products INNER JOIN products ON assembly_products.product_id = products.id AND order_assemblies.assembly_id = assembly_products.assembly_id WHERE order_assemblies.order_id = :id GROUP BY assembly_products.assembly_id")
    List<Integer> getTotalCostOrderAssembly(int id);

    @Query("SELECT assembly_id FROM order_assemblies WHERE order_id = :order_id")
    List<Integer> getAssembliesIDsByOrderID(int order_id);

    @Query("SELECT qty FROM order_assemblies WHERE order_id = :order_id")
    List<Integer> getQuantitiesByOrderID(int order_id);

    @Query("SELECT * FROM order_assemblies WHERE order_id = :order_id")
    List<OrderAssemblies> getOrdersAssembliesByOrderID(int order_id);

    @Delete
    void DeleteOrderAssemblies(OrderAssemblies orderAssembly);

    @Query("DELETE FROM order_assemblies WHERE order_id IN (:orders_ids)")
    void DeleteOrderAssembliesByOrdersID(int[] orders_ids);

}
