package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;

import java.util.List;


 class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView txt_first_name;
        private TextView txt_last_name;
        private TextView txt_address;
        private TextView txt_phone;
        private TextView txtx_email;

        private Customers customers;


        public ViewHolder(@NonNull final View itemView ) {
            super(itemView);


            txt_first_name =itemView.findViewById(R.id.txt_first_name);
            txt_last_name =itemView.findViewById(R.id.txt_last_name);
            txt_address =itemView.findViewById(R.id.txt_address);
            txt_phone =itemView.findViewById(R.id.txt_phone);
            txtx_email =itemView.findViewById(R.id.txt_email);


            final View parent = itemView;

            itemView.setOnCreateContextMenuListener(this);


        }
        public void bind(Customers customers) {
            this.customers = customers;

            txt_first_name.setText(customers.getFirstName());
            txt_last_name.setText(String.valueOf(customers.getLastName()));
            txt_address.setText(customers.getAddress());
            txt_phone.setText(String.valueOf(customers.getPhone1()));
            txtx_email.setText(String.valueOf(customers.getEmail()));



        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

           MenuItem information =  menu.add(this.getAdapterPosition(), 1, 0, "Information");
            MenuItem edit =menu.add(this.getAdapterPosition(), 2, 1, "Edit");
            MenuItem delete = menu.add(this.getAdapterPosition(), 3, 1, "Delete");

            information.setOnMenuItemClickListener(onEditMenu);
            edit.setOnMenuItemClickListener(onEditMenu);
            delete.setOnMenuItemClickListener(onEditMenu);

        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                            case 0:

                                //Dialogo para mosrar informacion completa
                                return true;

                            case 1:
                                
                                //nueva ventana para editar
                                return true;

                            case 2:
                                //Dialogo confirmacion de accion
                               default: return true;
                        }
            }
        };


    }



    private List<Customers> customers;
    public CustomerAdapter(List<Customers> customers){
        this.customers = customers;


    }

     @Override
     public int getItemViewType(int position) {
         return super.getItemViewType(position);
     }

     @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_clientes, viewGroup, false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(customers.get(i));
    }


    @Override
    public int getItemCount() {
        return customers.size();
    }


}
public class ClientesActivity extends AppCompatActivity  {



    public static String CLIENTES_FLAG_KEY = "CLIENTES_FLAG_KEY";
    public static final int CLIENTES_REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    public List<Customers> customersAll;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        Toolbar toolbar = findViewById(R.id.toolbar_clients);
        setSupportActionBar(toolbar);

        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
        CustomersDao dbCusDao = dbCus.customersDao();
        customersAll = dbCusDao.getAllCustomers();

        recyclerView = findViewById(R.id.recycler_clients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(customersAll);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent( ClientesActivity.this, MainActivity.class);
        ClientesActivity.super.finish();
        startActivityForResult(intent , MainActivity.PRINCIPAL_REQUEST_CODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clients_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_button_item:
//comenzamos la funcion de busqueda
                Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.add_button_item:
//agregamos un activity para anexar a nuevo usuario
                Toast.makeText(this, "New Activity...", Toast.LENGTH_SHORT).show();

                return true;


                default:
                    return super.onOptionsItemSelected(item);


        }

    }









}

