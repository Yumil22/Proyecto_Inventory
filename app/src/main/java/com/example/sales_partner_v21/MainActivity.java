package com.example.sales_partner_v21;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.sales_partner_v21.Database.AssembliesProducts;
import com.example.sales_partner_v21.Database.AssembliesProductsDao;
import com.example.sales_partner_v21.Database.Customers;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderAssemblies;
import com.example.sales_partner_v21.Database.OrderStatus;
import com.example.sales_partner_v21.Database.OrderStatusDao;
import com.example.sales_partner_v21.Database.Orders;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;
import com.example.sales_partner_v21.Database.Products;
import com.example.sales_partner_v21.Database.ProductsCategories;
import com.example.sales_partner_v21.Database.ProductsCategoriesDao;
import com.example.sales_partner_v21.Database.ProductsDao;
import com.example.sales_partner_v21.Database.Sellers;
import com.example.sales_partner_v21.Database.SellersDao;
import com.example.sales_partner_v21.SellersLogin.SellersLogin;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageButton productsBtn;
    ImageButton ReportsBtn;
    ImageButton OrdersBtn;
    ImageButton ClientsBtn;
    ImageButton AssembliesBtn;
    ImageButton InfoBtn;

    Button Login, Logout;

    Boolean log;
    int idSeller;
    private RequestQueue queue;

    public static final int PRINCIPAL_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        SharedPreferences configPreferences = getSharedPreferences("LOG", 0);
        log = configPreferences.getBoolean("CODIGOLOGIN", false);
        idSeller = configPreferences.getInt("IDSELLER", -1);

        Toast.makeText(this, "" + idSeller, Toast.LENGTH_SHORT).show();

        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        //final SellersDao sellersDao = database.sellersDao();
        final AssembliesDao assembliesDao = database.assembliesDao();
        final AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();
        final CustomersDao customersDao = database.customersDao();
        final OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();
        final OrdersDao ordersDao = database.ordersDao();
        final OrderStatusDao orderStatusDao = database.orderStatusDao();
        final ProductsCategoriesDao productsCategoriesDao = database.productsCategoriesDao();
        final ProductsDao productsDao = database.productsDao();
        assembliesDao.DeleteAssembliesTable();
        assembliesProductsDao.DeleteAssemblyProductsTable();
        customersDao.DeleteCustomersTable();
        ordersAssembliesDao.DeleteOrderAssembliesTable();
        ordersDao.DeleteOrdersTable();
        //orderStatusDao.DeleteOrderStatusTable();
        //productsCategoriesDao.DeleteProductCategoriesTable();
        //productsDao.DeleteProductsTable();


        //ACTUALIZO TODAS MIS TABLAS
        cargarWebServiseAll();

        //ACTUALIZO TODAS MIS TABLAS
        productsBtn = findViewById(R.id.productsBtn);
        ReportsBtn = findViewById(R.id.ReportsBtn);
        OrdersBtn = findViewById(R.id.OrdersBtn);
        ClientsBtn = findViewById(R.id.ClientsBtn);
        AssembliesBtn = findViewById(R.id.AssembliesBtn);
        InfoBtn = findViewById(R.id.InfoBtn);
        Login = findViewById(R.id.log_in_button);
        Logout = findViewById(R.id.log_out_button);



        if(log==true){
            Logout.setEnabled(true);
            Login.setEnabled(false);
            productsBtn.setEnabled(true);
            ReportsBtn.setEnabled(true);
            OrdersBtn.setEnabled(true);
            ClientsBtn.setEnabled(true);
            AssembliesBtn.setEnabled(true);
        }else if (log==false){

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Inicion de sesion");
            builder.setMessage("Es necesario iniciar sesion para acceder a las opciones");
            builder.setNeutralButton("Ok",null);
            Dialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);


            Logout.setEnabled(false);
            Login.setEnabled(true);
            productsBtn.setEnabled(false);
            ReportsBtn.setEnabled(false);
            OrdersBtn.setEnabled(false);
            ClientsBtn.setEnabled(false);
            AssembliesBtn.setEnabled(false);
        }

        productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent, ProductsActivity.PRODUCTS_REQUEST_CODE);
            }
        });

        ReportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ReportsActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent, ReportsActivity.REPORTS_REQUEST_CODE);
            }
        });

        OrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, OrdersActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent, OrdersActivity.ORDERS_REQUEST_CODE);
            }
        });

        ClientsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(MainActivity.this, ClientesActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent2, ClientesActivity.CLIENTES_REQUEST_CODE);
            }
        });

        AssembliesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EnsamblesActivity.class);
                MainActivity.super.finish();
                startActivityForResult(intent, EnsamblesActivity.ENSAMBLES_REQUEST_CODE);
            }
        });

        InfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Made in Mexico by Yumil Rueda Flores Trejo and Alejandro Matos")/*.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }) */;
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellersLogin.class);
                MainActivity.super.finish();
                startActivityForResult(intent, SellersLogin.SELLERS_REQUEST_CODE);
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Cerrar Sesion");
                builder.setMessage("No podras acceder a ninguna opción");
                builder.setPositiveButton("Cerrar sesion", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences newassembly = getSharedPreferences("LOG", 0);
                        SharedPreferences.Editor asemblyEditor = newassembly.edit();
                        asemblyEditor.putBoolean("CODIGOLOGIN", false);
                        asemblyEditor.apply();
                        Logout.setEnabled(false);
                        Login.setEnabled(true);
                        productsBtn.setEnabled(false);
                        ReportsBtn.setEnabled(false);
                        OrdersBtn.setEnabled(false);
                        ClientsBtn.setEnabled(false);
                        AssembliesBtn.setEnabled(false);
                        //sellersDao.DeleteSellersTable();

                    }
                });
                builder.setNegativeButton(android.R.string.cancel,null);
                Dialog dialog = builder.create();
                dialog.show();
                dialog.setCancelable(false);
            }
        });
    }

    private RequestQueue request;
    private RequestQueue request2;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private JSONArray jsonArray2;
    //String de assemblies
    private int id_assemblies  ;
    private String description_assemblies = "";
    private List<Assemblies> assembliesRemoteDatabase = new ArrayList<>();
    private List<Assemblies> assembliesRemoteDatabase2 = new ArrayList<>();

    //strings de assembly_products
    private String assembly_id_db = "";
    private String product_id_db = "";
    private String qty_db = "";
    private List<AssembliesProducts> assemblyProductsRemoteDatabase = new ArrayList<>();
    private List<AssembliesProducts> assemblyProductsRemoteDatabase2 = new ArrayList<>();


    //Strings de Customers
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

    //Strings de orders
    private String id_orders_db = "";
    private String status_id_orders_db = "";
    private String customer_id_orders_db = "";
    private String date_orders_db = "";
    private String change_log_db = "";
    private String seller_id_orders_db = "";

    private List<Orders> ordersRemoteDatabase = new ArrayList<>();
    private List<Orders> ordersRemoteDatabase2 = new ArrayList<>();


    //String de order_status
    private String id_orderStatus_db = "" ;
    private String description_orderStatus_db = "";
    private String editable_orderSatus_db = "";
    private String previous_orderStatus_db = "";
    private String next_orderStatus_db = "";
    private List<OrderStatus>  ordersStatusRemoteDatabase= new ArrayList<>();
    private List<OrderStatus>  ordersStatusRemoteDatabase2= new ArrayList<>();


    //String de PRODCUTS
    private String id_product_products = "" ;
    private String category_id_products = "";
    private String description_products = "";
    private String price_products = "";
    private String qty_products = "";
    private List<Products>  ProductsRemoteDatabase= new ArrayList<>();
    private List<Products>  ProductsRemoteDatabase2= new ArrayList<>();



    //String de PRODCUTS_category
    private String id_products_category = "" ;
    private String description_products_category = "";

    private List<ProductsCategories>  ProductsCategoryRemoteDatabase= new ArrayList<>();
    private List<ProductsCategories>  ProductsCategoryRemoteDatabase2= new ArrayList<>();



    //Strings de orders_assemblies
    private String order_id_oa = "";
    private String assembly_id_oa = "";
    private String qty_oa = "";


    private   String url =  "http://192.168.43.246:3000/assemblies/"  ;


    private List<OrderAssemblies> ordersAssembliesRemoteDatabase = new ArrayList<>();
    private List<OrderAssemblies> ordersAssembliesRemoteDatabase2 = new ArrayList<>();

    private JsonArrayRequest getRequest;

    private void cargarWebServiseAll() {

        //Llamado a la base de datos para la utilizacion de Dao's
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        final SellersDao sellersDao = database.sellersDao();
        final AssembliesDao assembliesDao = database.assembliesDao();
        final AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();
        final CustomersDao customersDao = database.customersDao();
        final OrdersAssembliesDao ordersAssembliesDao = database.ordersAssembliesDao();
        final OrdersDao ordersDao = database.ordersDao();
        final OrderStatusDao orderStatusDao = database.orderStatusDao();
        final ProductsCategoriesDao productsCategoriesDao = database.productsCategoriesDao();
        final ProductsDao productsDao = database.productsDao();
//Actualizo assemblies
        request = Volley.newRequestQueue(MainActivity.this);
        request2 = Volley.newRequestQueue(MainActivity.this);
        String url2 = "http://192.168.43.246:3000/assemblies/products/"  ;
        String url3 = "http://192.168.43.246:3000/customers/"  ;
        String url4 = "http://192.168.43.246:3000/order/"  ;
        String url5 = "http://192.168.43.246:3000/order/status/"  ;
        String url6 = "http://192.168.43.246:3000/products/"  ;
        String url7 = "http://192.168.43.246:3000/products/categories/"  ;
        String url8 = "http://192.168.43.246:3000/order/assemblies/"  ;

         getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray2 = response;

                        jsonObject = null;
                        try {
                            for(int i =0; i<= jsonArray2.length();i++){
                                jsonObject = jsonArray2.getJSONObject(i);
                                id_assemblies = jsonObject.getInt("id");
                                description_assemblies = jsonObject.getString("description");

                                //assembliesRemoteDatabase.add(new Assemblies( id_assemblies , description_assemblies ));

                                //ACTULIZACION DE LA TABLA
                                assembliesDao.InsertAssemblies(new Assemblies( id_assemblies , description_assemblies));
                            }
                            Toast.makeText(MainActivity.this, "ASSEMBLIES", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        assembliesRemoteDatabase2 = assembliesRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();


                    }
                }
        );

        request2.add(getRequest);


        //ACTUALIZO ASSEMBLIY_PRODUCTS

        JsonArrayRequest getRequest2 = new JsonArrayRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            for(int i =0; i<= jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                assembly_id_db = jsonObject.getString("assembly_id");
                                product_id_db = jsonObject.getString("product_id");
                                qty_db = jsonObject.getString("qty");

                                //assemblyProductsRemoteDatabase.add(new AssembliesProducts( Integer.parseInt(assembly_id_db) , Integer.parseInt(product_id_db), Integer.parseInt(qty_db) ));
                                //ACTUALIZACION DE LA TABLA
                                assembliesProductsDao.InsertAssemblyProducts(new AssembliesProducts( Integer.parseInt(assembly_id_db) , Integer.parseInt(product_id_db), Integer.parseInt(qty_db)));
                            }
                            Toast.makeText(MainActivity.this, "ASSEMBLIESPRODUCTS", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        assemblyProductsRemoteDatabase2 = assemblyProductsRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();


                    }
                }
        );

        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(getRequest2);


        //Actualizamos Customers


        JsonArrayRequest getRequest3 = new JsonArrayRequest(Request.Method.GET, url3, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
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
                            Toast.makeText(MainActivity.this, "CUSTOMERS", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MainActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();


                    }
                }
        );

        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(getRequest3);



        //ACTUALIZAMOS ORDERS

        JsonArrayRequest getRequest4 = new JsonArrayRequest(Request.Method.GET, url4, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            for(int i =0; i<= jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                id_orders_db = jsonObject.getString("id");
                                status_id_orders_db = jsonObject.getString("status_id");
                                customer_id_orders_db = jsonObject.getString("customer_id");
                                date_orders_db = jsonObject.getString("date");
                                change_log_db = jsonObject.getString("change_log");
                                seller_id_orders_db = jsonObject.getString("seller_id");

                               // ordersRemoteDatabase.add(new Orders( Integer.parseInt(id_orders_db) , Integer.parseInt(status_id_orders_db), Integer.parseInt(customer_id_orders_db),
                               //         date_orders_db, change_log_db, Integer.parseInt(seller_id_orders_db)));

                                //ACTUALIZACION DE LA TABLA
                                ordersDao.InsertOrders(new Orders( Integer.parseInt(id_orders_db) , Integer.parseInt(status_id_orders_db), Integer.parseInt(customer_id_orders_db),
                                        date_orders_db, change_log_db, Integer.parseInt(seller_id_orders_db)));
                            }
                            Toast.makeText(MainActivity.this, "ORDERS", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ordersRemoteDatabase2 = ordersRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();

                    }
                }
        );
        request.add(getRequest4);



        //ACTUALIZAMOS order_Status


        JsonArrayRequest getRequest5 = new JsonArrayRequest(Request.Method.GET, url5, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            for(int i =0; i<= jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                id_orderStatus_db = jsonObject.getString("id");
                                description_orderStatus_db = jsonObject.getString("description");
                                editable_orderSatus_db = jsonObject.getString("editable");
                                previous_orderStatus_db = jsonObject.getString("previous");
                                 next_orderStatus_db= jsonObject.getString("next");

                                ordersStatusRemoteDatabase.add(new OrderStatus( Integer.parseInt(id_orderStatus_db) , description_orderStatus_db, Integer.parseInt(editable_orderSatus_db),
                                        previous_orderStatus_db, next_orderStatus_db));
                                //ACTUALIZACION DE LA TABLA
                                //orderStatusDao.InsertOrderStatus(new OrderStatus( Integer.parseInt(id_orderStatus_db) , description_orderStatus_db, Integer.parseInt(editable_orderSatus_db),
                                  //      previous_orderStatus_db, next_orderStatus_db));
                            }
                            Toast.makeText(MainActivity.this, "ORDERSTATUS", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ordersRemoteDatabase2 = ordersRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();

                    }
                }
        );
        request.add(getRequest5);



        //ACCTIALIZAMOS PRODUCTOS


        JsonArrayRequest getRequest6 = new JsonArrayRequest(Request.Method.GET, url6, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            for(int i =0; i<= jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                id_product_products = jsonObject.getString("id");
                                description_products = jsonObject.getString("description");
                                category_id_products = jsonObject.getString("category_id");
                                price_products = jsonObject.getString("price");
                                qty_products= jsonObject.getString("qty");

                                ProductsRemoteDatabase.add(new Products( Integer.parseInt(id_product_products) ,  Integer.parseInt(category_id_products), description_products,
                                        Integer.parseInt( price_products), Integer.parseInt(qty_products)));

                                //ACTUALIACION DE LA TABLA
                               // productsDao.InsertProducts(new Products( Integer.parseInt(id_product_products) ,  Integer.parseInt(category_id_products), description_products,
                                 //       Integer.parseInt( price_products), Integer.parseInt(qty_products)));
                            }
                            Toast.makeText(MainActivity.this,  "PRODUCTS", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ProductsRemoteDatabase2 = ProductsRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();

                    }
                }
        );
        request.add(getRequest6);




        //ACCTIALIZAMOS PRODUCTs_category


        JsonArrayRequest getRequest7 = new JsonArrayRequest(Request.Method.GET, url7, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            for(int i =0; i<= jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                id_products_category = jsonObject.getString("id");
                                description_products_category = jsonObject.getString("description");

                                ProductsCategoryRemoteDatabase.add(new ProductsCategories( Integer.parseInt(id_products_category) ,  description_products_category));

                                //ACTUALIACION DE LA TABLA
                                //productsCategoriesDao.InsertProductCategories(new ProductsCategories( Integer.parseInt(id_products_category) ,  description_products_category));
                            }
                            Toast.makeText(MainActivity.this, "PRODUCTCATEGORIES", Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ProductsCategoryRemoteDatabase2 = ProductsCategoryRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();

                    }
                }
        );
        request.add(getRequest7);




        //ACCTIALIZAMOS order_assemblies


        JsonArrayRequest getRequest8 = new JsonArrayRequest(Request.Method.GET, url8, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray = response;

                        try {
                            for(int i =0; i<= jsonArray.length();i++){
                                jsonObject = jsonArray.getJSONObject(i);
                                order_id_oa     = jsonObject.getString("order_id");
                                assembly_id_oa = jsonObject.getString("assembly_id");
                                qty_oa        =  jsonObject.getString("qty");

                                ordersAssembliesRemoteDatabase.add(new OrderAssemblies( Integer.parseInt(order_id_oa) ,Integer.parseInt(assembly_id_oa), Integer.parseInt(qty_oa)));
                                //ACTUALIZACION DE LA TABLA
                                ordersAssembliesDao.InsertOrderAssemblies(new OrderAssemblies( Integer.parseInt(order_id_oa) ,Integer.parseInt(assembly_id_oa), Integer.parseInt(qty_oa)));
                            }
                            Toast.makeText(MainActivity.this, "ORDERASSEMBLIES", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ordersAssembliesRemoteDatabase2 = ordersAssembliesRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();

                    }
                }
        );
        request.add(getRequest8);

    }
}
