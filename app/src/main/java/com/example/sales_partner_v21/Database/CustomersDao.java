package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CustomersDao {

    @Query("SELECT * FROM CUSTOMERS ORDER BY id")
     List<Customers> getAllCustomers();

    @Query("SELECT * FROM customers WHERE id = :id")
    Customers getCustomerById(int id);

    @Query("SELECT last_name FROM CUSTOMERS")
    List<String> getAllCustomerLastNames();

    @Query("SELECT * FROM CUSTOMERS WHERE first_name LIKE '%' || :name  || '%' ")
    List<Customers> getCustomersbyFirstname (String name);

    @Query("SELECT * FROM CUSTOMERS WHERE last_name LIKE '%' || :last_name || '%'  ")
    List<Customers> getCustomersbyLastname (String last_name);

    @Query("SELECT * FROM CUSTOMERS WHERE address LIKE '%' || :address || '%'  ")
    List<Customers> getCustomersbyAddress (String address);

    @Query("SELECT * FROM CUSTOMERS WHERE email LIKE '%' || :email  || '%' ")
    List<Customers> getCustomersbyEmail (String email);

    @Query("SELECT * FROM CUSTOMERS WHERE phone1 LIKE '%' || :phone || '%' OR phone2 || '%' LIKE '%' || :phone || '%'OR phone3 LIKE '%' || :phone || '%'")
    List<Customers> getCustomersbyPhone (String phone);

    @Query("SELECT * FROM CUSTOMERS WHERE first_name LIKE '%' || :name  || '%' OR last_name LIKE '%' || :last_name || '%' OR address LIKE '%' || :address || '%' OR email LIKE '%' || :email  || '%' OR phone1 LIKE '%' || :phone || '%' OR phone2 || '%' LIKE '%' || :phone || '%'OR phone3 LIKE '%' || :phone || '%'")
    List<Customers> getCustomerByAll(String name, String last_name, String address, String email, String phone);

    @Query("SELECT id FROM CUSTOMERS ORDER BY id DESC LIMIT 1")
    int getMaxId();

    @Query("SELECT MAX(id) FROM customers")
    int getLastID();


    @Insert
    void InsertNewUser(Customers customer);

    @Delete
    void Deleteuser(Customers customers);

    @Query(" SELECT o.customer_id as id, c.first_name, c.last_name, c.address, c.phone1, c.phone2, c.phone3, c.email FROM customers c " +
            " INNER JOIN orders o ON c.id = o.customer_id " +
            " WHERE o.id = :oder")
    public Customers getcustomerbyOrderId(int oder);

    @Query("SELECT o.id FROM customers C " +
            " INNER JOIN orders o ON o.customer_id = c.id " +
            " INNER JOIN order_assemblies oa ON oa.order_id = o.id " +
            " INNER JOIN assembly_products ap ON ap.assembly_id = oa.assembly_id" +
            " INNER JOIN products p ON p.id = ap.product_id" +
            " WHERE o.status_id = 0 GROUP BY o.id ORDER BY SUM(p.price)DESC")
    List<Integer> getIdHighercount();

    @Query("SELECT o.id FROM customers C " +
            " INNER JOIN orders o ON o.customer_id = c.id " +
            " INNER JOIN order_assemblies oa ON oa.order_id = o.id " +
            " INNER JOIN assembly_products ap ON ap.assembly_id = oa.assembly_id" +
            " INNER JOIN products p ON p.id = ap.product_id" +
            " WHERE o.status_id = 0 GROUP BY o.id ORDER BY SUM(p.price)ASC")
    List<Integer> getIdLesscount();
}
