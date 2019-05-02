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

<<<<<<< HEAD

=======
>>>>>>> 93d2d557ff610215b3e942c79ba776616c1cd04c
    @Query("SELECT * FROM orders WHERE date >= :date " +
            "AND date <= :date2")
    public List<Orders> getordersbydate(String date, String date2);

    @Query("SELECT SUM(p.price * oa.qty) FROM orders o " +
            " INNER JOIN order_assemblies oa  ON o.id = oa.order_id" +
            " INNER JOIN assemblies a ON a.id = oa.assembly_id  " +
            " INNER JOIN assembly_products ap ON ap.assembly_id = a.id " +
            " INNER JOIN products p ON ap.product_id = p.id " +
            " WHERE date >= :date AND date <= :date2 AND o.status_id = 4 OR o.status_id = 3")
    public int getCountbyDate(String date, String date2);

    @Query("SELECT date FROM orders WHERE date >= (:Year +'-01-01') " +
            "AND date <= (:Year +'-12-31')")
    public List<String> getDatesbyyear(String Year);

    @Query("UPDATE orders SET status_id = :newStatusID WHERE id = :order_id")
    void UpdateStatusID(int order_id, int newStatusID);
<<<<<<< HEAD

=======
>>>>>>> 93d2d557ff610215b3e942c79ba776616c1cd04c

    @Query("SELECT  c.first_name FROM orders o " +
            "INNER JOIN customers c ON c.id = o.customer_id " +
            " WHERE status_id = 0")
    public List<String> getcustomerForComfirm();

    @Query(" SELECT count() FROM orders WHERE customer_id = :id_cus")
    int getOrdercountbyid(int id_cus);

    @Query(" SELECT o.id FROM orders o " +
            " INNER JOIN order_assemblies oa ON o.id = oa.order_id  " +
            " INNER JOIN assembly_products ap ON ap.assembly_id = oa.assembly_id " +
            " INNER JOIN products p ON p.id = ap.product_id " +
            "  WHERE o.status_id = 0 GROUP BY o.id ORDER BY SUM(p.price)DESC")
    List<Integer> getCountsOrders();

    @Query("SELECT * FROM orders WHERE customer_id = :customer_id")
    List<Orders> getOrdersByCustomerID(int customer_id);

    @Query("DELETE FROM orders WHERE id IN (:ids)")
    void DeleteOrdersByOrderID(int[] ids);
<<<<<<< HEAD

=======
>>>>>>> 93d2d557ff610215b3e942c79ba776616c1cd04c
}
