package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

@Entity(tableName = "order_assemblies")
public class OrderAssemblies {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "order_id")
    private int order_id;

    @NonNull
    @ColumnInfo(name = "assembly_id")
    private int assembly_id;

    @NonNull
    @ColumnInfo(name = "qty")
    private int qty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getAssembly_id() {
        return assembly_id;
    }

    public void setAssembly_id(int assembly_id) {
        this.assembly_id = assembly_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public OrderAssemblies(int order_id, int assembly_id, int qty) {
        this.order_id = order_id;
        this.assembly_id = assembly_id;
        this.qty = qty;
    }
}
