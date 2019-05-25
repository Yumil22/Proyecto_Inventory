package com.example.sales_partner_v21.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SellersDao {

    @Query("Select * from sellers " +
            "where sellers.user_name like (:user) and  sellers.password like (:password)")
    LiveData<Sellers> LoginSearch(String user, String password);

    @Query("select * from sellers " +
            "            where sellers.user_name like (:user) and  sellers.password like (:password) ")
    LiveData<Sellers> GetNameId(String user, String password);

    @Query("Delete from sellers")
    public void DeleteSellersTable();

    @Insert
    public void InsertSeller(Sellers sellers);
}
