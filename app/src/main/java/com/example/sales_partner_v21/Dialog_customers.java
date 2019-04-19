package com.example.sales_partner_v21;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.sales_partner_v21.Database.Customers;

public class Dialog_customers {
    public Dialog_customers(Context context, Customers customer){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_customers);

        TextView first_name = dialog.findViewById(R.id.txt_first_name_dialog);
        TextView last_name = dialog.findViewById(R.id.txt_last_name_dialog);
        TextView e_mail = dialog.findViewById(R.id.txt_email_dialog);
        TextView phone1 = dialog.findViewById(R.id.txt_phone1_dialog);
        TextView phone2 = dialog.findViewById(R.id.txt_phone2_dialog);
        TextView phone3 = dialog.findViewById(R.id.txt_phone3_dialog);
        TextView address = dialog.findViewById(R.id.txt_address_dialog);
        Button  ok = dialog.findViewById(R.id.button_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        first_name.setText(customer.getFirstName());
        last_name.setText(customer.getLastName());
        phone1.setText(customer.getPhone1());
        e_mail.setText(customer.getEmail());
        phone2.setText(customer.getPhone2());
        phone3.setText(customer.getPhone3());
        address.setText(customer.getAddress());


        dialog.show();
    }
}
