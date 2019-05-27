package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface OrdersDao {
    @Insert
    void InsertNewOrder(Orders order);

    @Query("SELECT MAX(id) FROM orders ")
    int getMaxID();

    @Query("SELECT * FROM orders WHERE customer_id IN (:ids) AND status_id IN (:statuses) and orders.seller_id in (:SellerId)")
    List<Orders> getFilterOrdersByIDAndStatus(int[] ids, int[] statuses, int SellerId);

    @Query("SELECT customers.* FROM orders INNER JOIN customers ON customers.id = orders.customer_id WHERE orders.id = :order_id")
    Customers getCustomerFromOrderID(int order_id);

    @Query("SELECT order_status.* FROM orders INNER JOIN order_status ON orders.status_id = order_status.id WHERE orders.id = :order_id")
    OrderStatus getOrderStatusFromOrderID(int order_id);

    @Query("SELECT * FROM orders WHERE id = :order_id")
    Orders getOrderByID(int order_id);

    @Query("SELECT * FROM orders WHERE status_id = 0 and seller_id in (:SellerId)")
    public List<Orders> getordersforComfirm(int SellerId);

    @Query("SELECT * FROM orders")
    public List<Orders> getAllorders();

    @Query("SELECT date FROM orders " +
            "where seller_id in (:SellerId) ")
    public List<String> getDates(int SellerId);

    @Query("SELECT * FROM orders WHERE date >= :date " +
            "AND date <= :date2 and seller_id in (:SellerId)")
    public List<Orders> getordersbydate(String date, String date2, int SellerId);

    @Query("SELECT SUM(p.price * oa.qty) FROM orders o " +
            " INNER JOIN order_assemblies oa  ON o.id = oa.order_id" +
            " INNER JOIN assembly_products ap ON ap.assembly_id = oa.assembly_id " +
            " INNER JOIN products p ON ap.product_id = p.id " +
            " WHERE date >= :date AND date <= :date2 and seller_id in (:SellerId)")
    public int getCountbyDate(String date, String date2, int SellerId);
    //este es el superior, no el que es por orden

    @Query("SELECT date FROM orders WHERE date >= (:Year +'-01-01') " +
            "AND date <= (:Year +'-12-31')")
    public List<String> getDatesbyyear(String Year);

    @Query("UPDATE orders SET status_id = :newStatusID WHERE id = :order_id")
    void UpdateStatusID(int order_id, int newStatusID);


    @Query("SELECT  c.first_name FROM orders o " +
            "INNER JOIN customers c ON c.id = o.customer_id " +
            " WHERE o.status_id = 0 and seller_id in (:SellerId)")
    public List<String> getcustomerForComfirm(int SellerId);

    @Query(" SELECT count() FROM orders WHERE customer_id = :id_cus")
    int getOrdercountbyid(int id_cus);

    @Query(" SELECT o.id FROM orders o " +
            " INNER JOIN order_assemblies oa ON o.id = oa.order_id  " +
            " INNER JOIN assembly_products ap ON ap.assembly_id = oa.assembly_id " +
            " INNER JOIN products p ON p.id = ap.product_id " +
            "  WHERE o.status_id = 0 and o.seller_id in (:IdSeller) GROUP BY o.id ORDER BY o.date DESC")
    List<Integer> getCountsOrders(int IdSeller);

    @Query("SELECT * FROM orders WHERE customer_id = :customer_id")
    List<Orders> getOrdersByCustomerID(int customer_id);

    @Query("DELETE FROM orders WHERE id IN (:ids)")
    void DeleteOrdersByOrderID(int[] ids);

    @Query("UPDATE orders SET date = :newDate WHERE id = :order_id")
    void UpdateDate(int order_id, String newDate);

    @Query("SELECT * FROM orders WHERE date BETWEEN :initialDate AND :finalDate AND customer_id IN (:ids) AND status_id IN (:statuses) and seller_id in (:SellerId);")
    List<Orders> getFilterOrders(String initialDate, String finalDate, int[] ids, int[] statuses, int SellerId);

    @Query("SELECT * FROM orders WHERE date BETWEEN (SELECT MIN(date) FROM orders) AND :finalDate AND customer_id IN (:ids) AND status_id IN (:statuses) and seller_id in (:SellerId) ORDER BY id DESC")
    List<Orders> getFilterOrderByFinalDate(String finalDate, int[] ids, int[] statuses,int SellerId);

    @Query("Delete from orders")
    public void DeleteOrdersTable();

    @Insert
    void InsertOrders(Orders orders);
}
