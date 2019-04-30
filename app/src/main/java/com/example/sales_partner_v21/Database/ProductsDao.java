package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductsDao {
    @Query("SELECT * FROM products WHERE id IN (:productsIDs)")
    List<Products> getProductsByIDs(int[] productsIDs);

    @Query("SELECT * FROM products WHERE id = :id")
    Products getProductByID(int id);

    @Query("SELECT * FROM products WHERE category_id = :categoryID ORDER BY description")
    List<Products> getProductsByCategoryID(int categoryID);

    @Query("SELECT * FROM products ORDER BY description")
    List<Products> getAllProducts();

    @Query("SELECT * FROM products WHERE category_id = :categoryID AND description LIKE '%' || :descriptionSearch  || '%' ORDER BY description")
    List<Products> getProductsByCategoryIDAndDescription(int categoryID, String descriptionSearch);

    @Query("SELECT * FROM products WHERE description LIKE '%' || :descriptionSearch  || '%' ORDER BY description")
    List<Products> getProductsByDescription(String descriptionSearch);

    @Query(" SELECT p.id, category_id, description, price, p.qty FROM products p " +
            " INNER JOIN assembly_products ap ON ap.product_id = p.id " +
            " INNER JOIN order_assemblies os ON ap.assembly_id = os.assembly_id " +
            " INNER JOIN orders o ON os.order_id = o.id WHERE o.status_id = 0 GROUP BY product_id ")
    List<Products> getProductsMissing();

    @Query("SELECT p.id, p.qty as category_id ,description, price, qty_N as qty FROM products p " +
            "INNER JOIN  (SELECT os.id , o.status_id, ap.product_id, SUM(ap.qty * os.qty ) AS qty_N  FROM assembly_products ap " +
            "INNER JOIN order_assemblies os ON ap.assembly_id = os.assembly_id " +
            "INNER JOIN orders o ON os.order_id = o.id WHERE o.status_id = 0 GROUP BY product_id) ON p.id = product_id")
    List<Products> getProductsMissing2();

    @Query(" SELECT p.id, p.category_id ,description, price, oa.qty * ap.qty as qty FROM customers c " +
            " INNER JOIN orders o ON o.customer_id = c.id " +
            " INNER JOIN order_assemblies oa ON oa.order_id = o.id " +
            " INNER JOIN assembly_products ap ON ap.assembly_id = oa.assembly_id " +
            " INNER JOIN products p ON p.id = ap.product_id " +
            " WHERE c.id = :customerid AND o.status_id = 0")
    List<Products> getproductsNeeded(int customerid);

    @Query(" SELECT p.id, p.qty AS category_id ,description, price, oa.qty * ap.qty as qty FROM customers c " +
            " INNER JOIN orders o ON o.customer_id = c.id " +
            " INNER JOIN order_assemblies oa ON oa.order_id = o.id " +
            " INNER JOIN assembly_products ap ON ap.assembly_id = oa.assembly_id " +
            " INNER JOIN products p ON p.id = ap.product_id " +
            " WHERE c.id = :customerid AND o.status_id = 0")
    List<Products> getproductsNeededbyId(int customerid);

    @Query("SELECT p.id, p.category_id ,description, price, oa.qty * ap.qty as qty FROM orders o " +
            " INNER JOIN order_assemblies oa ON o.id = oa.order_id  " +
            " INNER JOIN assembly_products ap ON ap.assembly_id = oa.assembly_id " +
            " INNER JOIN products p ON p.id = ap.product_id " +
            " WHERE o.id = :orderId")
    List<Products> getproductsNeededbyOrderId(int orderId);

}
