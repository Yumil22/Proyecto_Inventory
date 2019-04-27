package com.example.sales_partner_v21;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderStatusAdapter extends ArrayAdapter<OrderStatusItem> {

    public OrderStatusAdapter(Context context, ArrayList<OrderStatusItem> orderStatusList){
        super(context,0,orderStatusList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_status_spinner,parent,false);
        }

        CheckBox orderStatus = convertView.findViewById(R.id.order_status);
        OrderStatusItem currentItem = getItem(position);

        if (currentItem != null){
            orderStatus.setText(currentItem.getOrderStatus());
        }
        return convertView;
    }
}



