package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.Date;

import javax.annotation.Nullable;

@Entity(tableName = "orders")
public class Orders {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "status_id")
    private int status_id;

    @NonNull
    @ColumnInfo(name = "customer_id")
    private int customer_id;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @Nullable
    @ColumnInfo(name = "change_log")
    private String change_log;

    @NonNull
    @ColumnInfo(name = "seller_id")
    private int seller_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public String getChange_log() {
        return change_log;
    }

    public void setChange_log(@NonNull String change_log) {
        this.change_log = change_log;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public Orders(int id, int status_id, int customer_id, @NonNull String date, @Nullable String change_log, int seller_id) {
        this.id = id;
        this.status_id = status_id;
        this.customer_id = customer_id;
        this.date = date;
        this.change_log = change_log;
        this.seller_id = seller_id;
    }
}
