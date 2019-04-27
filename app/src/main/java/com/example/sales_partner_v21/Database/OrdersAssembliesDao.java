package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrdersAssembliesDao {
    @Insert
    void InserNewOrdersAssembly(OrderAssemblies orderAssembly);


}
