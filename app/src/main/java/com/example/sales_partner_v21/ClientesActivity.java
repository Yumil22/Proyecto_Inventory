package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Assemblies;
import com.example.sales_partner_v21.Database.AssembliesDao;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;
import com.example.sales_partner_v21.Database.SellersDao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView txt_first_name;
        private TextView txt_last_name;
        private TextView txt_address;
        private TextView txt_phone;
        private TextView txtx_email;

        private Customers customers;
        private Context context;

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
        public void bind(Customers customers, Context context) {
            this.customers = customers;
            this.context = context;

            txt_first_name.setText(customers.getFirstName());
            txt_last_name.setText(String.valueOf(customers.getLastName()));
            txt_address.setText(customers.getAddress());
            txt_phone.setText(String.valueOf(customers.getPhone1()));
            txtx_email.setText(String.valueOf(customers.getEmail()));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            AppDatabase database = AppDatabase.getAppDatabase(context.getApplicationContext());
            OrdersDao ordersDao = database.ordersDao();
            MenuItem information =  menu.add(this.getAdapterPosition(), 1, 0, "Información");
            MenuItem edit =menu.add(this.getAdapterPosition(), 2, 0, "Editar");
            if (ordersDao.getOrdersByCustomerID(customers.getId()).isEmpty()){
                MenuItem delete = menu.add(this.getAdapterPosition(), 3, 0, "Eliminar");
                delete.setOnMenuItemClickListener(onEditMenu);
            }
            information.setOnMenuItemClickListener(onEditMenu);
            edit.setOnMenuItemClickListener(onEditMenu);
        }

        //public RequestQueue request;
        public String CUSTOMER_ID = "CUSTOMER_ID";
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1){
                    new Dialog_customers(itemView.getContext(),customers);
                    return true;
                } else if (item.getItemId() == 2) {
                    Intent intent2 = new Intent(itemView.getContext(), edit_custormer.class);
                    intent2.putExtra(CUSTOMER_ID, customers.getId());
                    itemView.getContext().startActivity(intent2);
                    return true;
                } else if (item.getItemId() == 3){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("¿Desea eliminar el cliente?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppDatabase database = AppDatabase.getAppDatabase(context.getApplicationContext());
                            CustomersDao customersDao = database.customersDao();
                            //---------------------------------
                            deleteClient(customers.getId());
                            Toast.makeText(itemView.getContext(), "" + customers.getId(), Toast.LENGTH_LONG).show();
                            //----------------------------------
                            //customersDao.Deleteuser(customers);
                            cargarWebServiseAssembly();
                            ((ClientesActivity)context).recreate();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    builder.show();
                    return true;
                }
                else {
                    return true;
                }
            }
        };
        //------------------------------------------------------------------------------------------
        public RequestQueue request =  Volley.newRequestQueue(itemView.getContext());
        private void deleteClient(int id_order){
            String urlF = "http://192.168.1.101:3000/customers/delete/"+ String.valueOf(id_order);

            JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.DELETE, urlF, null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response) {


//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(EditOrder.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();
                        }
                    }
            );
            request.add(getRequest);
        }


        private RequestQueue request2;
        private JSONObject jsonObject;
        private JSONArray jsonArray;
        private JSONArray jsonArray2;
        //String de assemblies
        private String id_customer_db = "";
        private String first_name_db = "";
        private String last_name_db = "";
        private String adresss_db = "";
        private String phone_1_db = "";
        private String phone_2_db = "";
        private String phone_3_db = "";
        private String email_db = "";
        private String active_db = ""; //se vuelve int
        private List<Customers> customersRemoteDatabase = new ArrayList<>();
        private List<Customers> customersRemoteDatabase2 = new ArrayList<>();

        private JsonArrayRequest getRequest;

        private void cargarWebServiseAssembly() {

            //Llamado a la base de datos para la utilizacion de Dao's

//Actualizo assemblies
            request = Volley.newRequestQueue(itemView.getContext());
            request2 = Volley.newRequestQueue(itemView.getContext());
            String url3 = "http://192.168.1.101:3000/customers/"  ;
            AppDatabase database = AppDatabase.getAppDatabase(itemView.getContext());
            final CustomersDao customersDao = database.customersDao();

            JsonArrayRequest getRequest3 = new JsonArrayRequest(Request.Method.GET, url3, null,
                    new Response.Listener<JSONArray>()
                    {
                        @Override
                        public void onResponse(JSONArray response) {

                            jsonArray = response;

                            try {
                                customersDao.DeleteCustomersTable();
                                for(int i =0; i<= jsonArray.length();i++){
                                    jsonObject = jsonArray.getJSONObject(i);
                                    id_customer_db = jsonObject.getString("id");
                                    first_name_db = jsonObject.getString("first_name");
                                    last_name_db = jsonObject.getString("last_name");
                                    adresss_db = jsonObject.getString("address");
                                    phone_1_db = jsonObject.getString("phone1");
                                    phone_2_db = jsonObject.getString("phone2");
                                    phone_3_db = jsonObject.getString("phone3");
                                    email_db = jsonObject.getString("email");
                                    active_db = jsonObject.getString("active");
                                    //customersRemoteDatabase.add(new Customers(Integer.parseInt(id_customer_db) ,first_name_db, last_name_db, adresss_db, phone_1_db, phone_2_db, phone_3_db, email_db,Integer.parseInt(active_db) ));
                                    //Toast.makeText(SellersLogin.this, first_name, Toast.LENGTH_LONG).show();

                                    //ACTUALIZACION DE LA TABLA
                                    customersDao.InsertCustomers(new Customers(Integer.parseInt(id_customer_db) ,first_name_db, last_name_db, adresss_db, phone_1_db, phone_2_db, phone_3_db, email_db,Integer.parseInt(active_db)));
                                }
                                Toast.makeText(itemView.getContext(), "CUSTOMERS", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            customersRemoteDatabase2 = customersRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(itemView.getContext(), error.toString() + "FUCK", Toast.LENGTH_LONG).show();


                        }
                    }
            );

            //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
            request.add(getRequest3);
        }
    }



    private List<Customers> customers;
    private Context context;
    public CustomerAdapter(List<Customers> customers, Context context){
        this.customers = customers;
        this.context = context;
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
        viewHolder.bind(customers.get(i),context);
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }
}

public class ClientesActivity extends AppCompatActivity  implements MultiSpinner.MultiSpinnerListener{
    public static String CLIENTES_FLAG_KEY = "CLIENTES_FLAG_KEY";
    public static final int CLIENTES_REQUEST_CODE = 1;

    private  Toolbar toolbar;
    private Spinner customerSpinner;
    private RecyclerView recyclerView;
    private CustomerAdapter adapter;
    private List<Customers> customersAll = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterCustomer;
    private EditText searchCustomer;
    private ArrayList<String> customersList = new ArrayList<>();
    private CustomersDao dbCusDao;
    private List<Integer> optionsSelected = new ArrayList<>();


    private boolean control_search = false;
    public String searched;


    static final String CONTROL = "SAVED_CONTROL";
    static final String SEARCHED = "SAVED_SEARCHED";


    public AppDatabase database;
    public  CustomersDao customersDao;
    public   List<Customers> check_customer= new ArrayList<>();
    public String text_edittext;

    @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        toolbar = findViewById(R.id.toolbar_clients);
        setSupportActionBar(toolbar);

        AppDatabase dbCus = AppDatabase.getAppDatabase(getApplicationContext());
        dbCusDao = dbCus.customersDao();

        //customersAll = dbCusDao.getAllCustomers();


        searchCustomer = findViewById(R.id.search_customer);

        MultiSpinner customerSpinner = findViewById(R.id.spinner_checkbox);
        recyclerView = findViewById(R.id.recycler_clients);

        customersList.add("First Name");
        customersList.add("Last Name");
        customersList.add("Address");
        customersList.add("E-mail");
        customersList.add("Phone");

        int counter = 0;
        for (int i = 0; i < 5; i++){
            optionsSelected.add(counter);
            counter++;
        }

        customerSpinner.setItems(customersList,getString(R.string.for_all),this,optionsSelected);

        if(savedInstanceState != null){

            control_search = savedInstanceState.getBoolean(CONTROL);
            text_edittext = savedInstanceState.getString(SEARCHED);
            optionsSelected = savedInstanceState.getIntegerArrayList("SELECTED_OPTIONS");

            customerSpinner.setItems(customersList,getString(R.string.for_all),this,optionsSelected);


            if( control_search== true   && !text_edittext.isEmpty()){

                customersAll = new ArrayList<>();
                check_customer = new ArrayList<>();
                customersAll.clear();


                if (optionsSelected.contains(0)){// STRING NAME
                    customersAll.addAll(dbCusDao.getCustomersbyFirstname(text_edittext));
                }
                if (optionsSelected.contains(1)){ // APELLIDO
                    check_customer = new ArrayList<>();

                    check_customer = dbCusDao.getCustomersbyLastname(text_edittext);
                    if(!check_customer.isEmpty()){
                        for(int i =0; i<customersAll.size(); i++){

                            for(int a=0; a< check_customer.size(); a++){
                                if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                    check_customer.remove(check_customer.get(a));
                                }

                            }
                        }
                        if(check_customer.size() != 0){
                            customersAll.addAll(check_customer);
                        }
                    }


                }
                if (optionsSelected.contains(2)) { // DIRECCION
                    check_customer = new ArrayList<>();

                    check_customer = dbCusDao.getCustomersbyAddress(text_edittext);

                    if(!check_customer.isEmpty()){
                        for(int i =0; i<customersAll.size(); i++){

                            for(int a=0; a< check_customer.size(); a++){
                                if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                    check_customer.remove(check_customer.get(a));
                                }

                            }
                        }
                        if(check_customer.size() != 0){
                            customersAll.addAll(check_customer);
                        }
                    }

                }
                if (optionsSelected.contains(3)){ // EMAIL
                    check_customer = new ArrayList<>();

                    check_customer = dbCusDao.getCustomersbyEmail(text_edittext);
                    if(!check_customer.isEmpty()){
                        for(int i =0; i<customersAll.size(); i++){

                            for(int a=0; a< check_customer.size(); a++){
                                if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                    check_customer.remove(check_customer.get(a));
                                }

                            }
                        }
                        if(check_customer.size() != 0){
                            customersAll.addAll(check_customer);
                        }
                    }

                }
                if (optionsSelected.contains(4)){ // TELEFONO
                    check_customer = new ArrayList<>();

                    check_customer =dbCusDao.getCustomersbyPhone(text_edittext);
                    if(!check_customer.isEmpty()){
                        for(int i =0; i<customersAll.size(); i++){

                            for(int a=0; a< check_customer.size(); a++){
                                if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                    check_customer.remove(check_customer.get(a));
                                }

                            }
                        }
                        if(check_customer.size() != 0){
                            customersAll.addAll(check_customer);
                        }
                    }

                }
            }

          // recyclerView.setLayoutManager(new LinearLayoutManager(this));
          // adapter = new CustomerAdapter(customersAll,this);
          // recyclerView.setAdapter(adapter);

        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomerAdapter(customersAll,this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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
        if (item.getItemId() == R.id.search_button_item){
            Toast.makeText(this, "Buscando...", Toast.LENGTH_SHORT).show();


            customersAll.clear();
            database = AppDatabase.getAppDatabase(getApplicationContext());
            customersDao = database.customersDao();

            control_search = true;
            //indica que se realizo la busqueda

            customersAll = new ArrayList<>();
            check_customer = new ArrayList<>();

            text_edittext = searchCustomer.getText().toString();
            //guarda el string buscado
            if(!text_edittext.isEmpty()){


            if (optionsSelected.contains(0)){// STRING NAME
                customersAll.addAll(customersDao.getCustomersbyFirstname(text_edittext));
            }
            if (optionsSelected.contains(1)){ // APELLIDO
                check_customer = new ArrayList<>();

                check_customer = customersDao.getCustomersbyLastname(text_edittext);
                if(!check_customer.isEmpty()){
                    for(int i =0; i<customersAll.size(); i++){

                        for(int a=0; a< check_customer.size(); a++){
                            if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                check_customer.remove(check_customer.get(a));
                            }

                        }
                    }
                    if(check_customer.size() != 0){
                        customersAll.addAll(check_customer);
                    }
                }


            }
            if (optionsSelected.contains(2)) { // DIRECCION
                check_customer = new ArrayList<>();

                check_customer = customersDao.getCustomersbyAddress(text_edittext);

                if(!check_customer.isEmpty()){
                    for(int i =0; i<customersAll.size(); i++){

                        for(int a=0; a< check_customer.size(); a++){
                            if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                check_customer.remove(check_customer.get(a));
                            }

                        }
                    }
                    if(check_customer.size() != 0){
                        customersAll.addAll(check_customer);
                    }
                }

            }
            if (optionsSelected.contains(3)){ // EMAIL
                check_customer = new ArrayList<>();

                check_customer = customersDao.getCustomersbyEmail(text_edittext);
                if(!check_customer.isEmpty()){
                    for(int i =0; i<customersAll.size(); i++){

                        for(int a=0; a< check_customer.size(); a++){
                            if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                check_customer.remove(check_customer.get(a));
                            }

                        }
                    }
                    if(check_customer.size() != 0){
                        customersAll.addAll(check_customer);
                    }
                }

            }
            if (optionsSelected.contains(4)){ // TELEFONO
                check_customer = new ArrayList<>();

                check_customer =customersDao.getCustomersbyPhone(text_edittext);
                if(!check_customer.isEmpty()){
                    for(int i =0; i<customersAll.size(); i++){

                        for(int a=0; a< check_customer.size(); a++){
                            if(customersAll.get(i).getId() == check_customer.get(a).getId()){
                                check_customer.remove(check_customer.get(a));
                            }

                        }
                    }
                    if(check_customer.size() != 0){
                        customersAll.addAll(check_customer);
                    }
                }

            }
            }
            else {
                customersAll = customersDao.getAllCustomers();
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CustomerAdapter(customersAll,this);
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    new LinearLayoutManager(this).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            return true;
        }
        else if (item.getItemId() == R.id.add_button_item){
            //agregamos un activity para anexar a nuevo usuario
            Intent intent2 = new Intent(ClientesActivity.this, new_user.class);
            //ClientesActivity.super.finish();
            startActivityForResult(intent2, new_user.NEW_USER_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

            outState.putBoolean(CONTROL, control_search);
            outState.putString(SEARCHED, text_edittext);

        outState.putIntegerArrayList("SELECTED_OPTIONS",new ArrayList<>(optionsSelected));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemsSelected(boolean[] selected) {
        if (selected[0]){
            if (!optionsSelected.contains(0)){
                optionsSelected.add(0);
            }
        }
        else {
            optionsSelected.remove(Integer.valueOf(0));
        }
        if (selected[1]){
            if (!optionsSelected.contains(1)){
                optionsSelected.add(1);
            }
        }
        else{
            optionsSelected.remove(Integer.valueOf(1));
        }
        if (selected[2]){
            if (!optionsSelected.contains(2)){
                optionsSelected.add(2);
            }
        }
        else{
            optionsSelected.remove(Integer.valueOf(2));
        }
        if (selected[3]){
            if (!optionsSelected.contains(3)){
                optionsSelected.add(3);
            }
        }
        else{
            optionsSelected.remove(Integer.valueOf(3));
        }
        if (selected[4]){
            if (!optionsSelected.contains(4)){
                optionsSelected.add(4);
            }
        }
        else{
            optionsSelected.remove(Integer.valueOf(4));
        }
    }

    private RequestQueue request;
    private RequestQueue request2;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private JSONArray jsonArray2;
    //String de assemblies
    private String id_customer_db = "";
    private String first_name_db = "";
    private String last_name_db = "";
    private String adresss_db = "";
    private String phone_1_db = "";
    private String phone_2_db = "";
    private String phone_3_db = "";
    private String email_db = "";
    private String active_db = ""; //se vuelve int
    private List<Customers> customersRemoteDatabase = new ArrayList<>();
    private List<Customers> customersRemoteDatabase2 = new ArrayList<>();

    private JsonArrayRequest getRequest;

    private void cargarWebServiseAssembly() {

        //Llamado a la base de datos para la utilizacion de Dao's

//Actualizo assemblies
        request = Volley.newRequestQueue(ClientesActivity.this);
        request2 = Volley.newRequestQueue(ClientesActivity.this);
        String url3 = "http://192.168.1.101:3000/customers/"  ;

        JsonArrayRequest getRequest3 = new JsonArrayRequest(Request.Method.GET, url3, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            customersDao.DeleteCustomersTable();
                            for(int i =0; i<= jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                id_customer_db = jsonObject.getString("id");
                                first_name_db = jsonObject.getString("first_name");
                                last_name_db = jsonObject.getString("last_name");
                                adresss_db = jsonObject.getString("address");
                                phone_1_db = jsonObject.getString("phone1");
                                phone_2_db = jsonObject.getString("phone2");
                                phone_3_db = jsonObject.getString("phone3");
                                email_db = jsonObject.getString("email");
                                active_db = jsonObject.getString("active");
                                //customersRemoteDatabase.add(new Customers(Integer.parseInt(id_customer_db) ,first_name_db, last_name_db, adresss_db, phone_1_db, phone_2_db, phone_3_db, email_db,Integer.parseInt(active_db) ));
                                //Toast.makeText(SellersLogin.this, first_name, Toast.LENGTH_LONG).show();

                                //ACTUALIZACION DE LA TABLA
                                customersDao.InsertCustomers(new Customers(Integer.parseInt(id_customer_db) ,first_name_db, last_name_db, adresss_db, phone_1_db, phone_2_db, phone_3_db, email_db,Integer.parseInt(active_db)));
                            }
                            Toast.makeText(ClientesActivity.this, "CUSTOMERS", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        customersRemoteDatabase2 = customersRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ClientesActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();


                    }
                }
        );

        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(getRequest3);
    }
}

