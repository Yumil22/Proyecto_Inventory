package com.example.sales_partner_v21.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "order_status")
public class OrderStatus {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "editable")
    private int editable;

    @NonNull
    @ColumnInfo(name = "previous")
    private String previous;

    @NonNull
    @ColumnInfo(name = "next")
    private String next;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    @NonNull
    public String getPrevious() {
        return previous;
    }

    public void setPrevious(@NonNull String previous) {
        this.previous = previous;
    }

    @NonNull
    public String getNext() {
        return next;
    }

    public void setNext(@NonNull String next) {
        this.next = next;
    }

    public OrderStatus(int id, @NonNull String description, int editable, @NonNull String previous, @NonNull String next) {
        this.id = id;
        this.description = description;
        this.editable = editable;
        this.previous = previous;
        this.next = next;
    }
}
