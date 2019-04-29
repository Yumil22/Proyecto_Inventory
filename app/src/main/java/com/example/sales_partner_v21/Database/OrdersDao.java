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

    @Query("SELECT * FROM orders WHERE date BETWEEN :initialDate AND :finalDate AND customer_id IN (:ids) AND status_id IN (:statuses);")
    List<Orders> getFilterOrders(String initialDate, String finalDate, int[] ids, int[] statuses);

    @Query("SELECT * FROM orders WHERE customer_id IN (:ids) AND status_id IN (:statuses)")
    List<Orders> getFilterOrdersByIDAndStatus(int[] ids, int[] statuses);

    @Query("SELECT customers.* FROM orders INNER JOIN customers ON customers.id = orders.customer_id WHERE orders.id = :order_id")
    Customers getCustomerFromOrderID(int order_id);

    @Query("SELECT order_status.* FROM orders INNER JOIN order_status ON orders.status_id = order_status.id WHERE orders.id = :order_id")
    OrderStatus getOrderStatusFromOrderID(int order_id);

    @Query("SELECT * FROM orders WHERE id = :order_id")
    Orders getOrderByID(int order_id);

    @Query("SELECT * FROM orders WHERE status_id = 0")
    public List<Orders> getordersforComfirm();


    @Query("SELECT * FROM orders")
    public List<Orders> getAllorders();

    @Query("SELECT date FROM orders")
    public List<String> getDates();

    @Query("SELECT date FROM orders WHERE date >= (:Year +'-01-01') " +
            "AND date <= (:Year +'-12-31')")
    public List<String> getDatesbyyear(String Year);

}
