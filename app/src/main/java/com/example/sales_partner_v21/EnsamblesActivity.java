package com.example.sales_partner_v21;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.sales_partner_v21.Database.AssembliesProductsDao;
import com.example.sales_partner_v21.Database.CustomersDao;
import com.example.sales_partner_v21.Database.OrderStatusDao;
import com.example.sales_partner_v21.Database.OrdersAssembliesDao;
import com.example.sales_partner_v21.Database.OrdersDao;
import com.example.sales_partner_v21.Database.ProductsCategoriesDao;
import com.example.sales_partner_v21.Database.ProductsDao;
import com.example.sales_partner_v21.Database.SellersDao;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

class AssembliesAdapter extends RecyclerView.Adapter<AssembliesAdapter.ViewHolder> {
    private List<Assemblies> assembliesList;
    private List<Integer> assembliesProductsList;
    private List<Integer> assembliesTotalCostList;
    private Context context;
    private ViewHolder.onAssemblyListener onAssemblyListener;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDescription;
        private TextView txtNumberProducts;
        private TextView txtTotalCost;

        private Assemblies assemblies;
        private int assembliesProducts;
        private int assembliesTotalCost;

        onAssemblyListener onAssemblyListener;
        NumberFormat formatter = new DecimalFormat("#,###");

        public ViewHolder(@NonNull final View itemView, onAssemblyListener onAssemblyListener){
            super(itemView);

            txtDescription = itemView.findViewById(R.id.description);
            txtNumberProducts = itemView.findViewById(R.id.number_products);
            txtTotalCost = itemView.findViewById(R.id.total_cost);
            this.onAssemblyListener = onAssemblyListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Assemblies assemblies, int assembliesProducts, int assembliesTotalCost) {
            this.assemblies = assemblies;
            this.assembliesProducts = assembliesProducts;
            this.assembliesTotalCost = assembliesTotalCost;
            txtDescription.setText(assemblies.getDescription());
            txtNumberProducts.setText(String.valueOf(assembliesProducts));
            txtTotalCost.setText("$ " + formatter.format(assembliesTotalCost));
        }

        @Override
        public void onClick(View v) {
            onAssemblyListener.OnAssemblyClick(getAdapterPosition());
        }

        public interface onAssemblyListener{
            void OnAssemblyClick(int position);
        }
    }

    public AssembliesAdapter(List<Assemblies> assembliesList,List<Integer> assembliesProductsList,List<Integer> assembliesTotalCostList, Context context, ViewHolder.onAssemblyListener assemblyListener){
        this.assembliesList = assembliesList;
        this.assembliesProductsList = assembliesProductsList;
        this.assembliesTotalCostList = assembliesTotalCostList;
        this.context = context;
        this.onAssemblyListener = assemblyListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.assembly_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view,onAssemblyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(assembliesList.get(position),assembliesProductsList.get(position),assembliesTotalCostList.get(position));
    }

    @Override
    public int getItemCount() {
        return assembliesList.size();
    }
}

public class EnsamblesActivity extends AppCompatActivity implements AssembliesAdapter.ViewHolder.onAssemblyListener{

    public static final int ENSAMBLES_REQUEST_CODE =1;
    private RecyclerView assembliesRecyclerView;
    private EditText searchTextOption;
    List<Assemblies> assemblies;
    List<Integer> assembliesProducts = new ArrayList<>();
    List<Integer> assembliesTotalCost = new ArrayList<>();
    public String SEARCH_TEXT = "SEARCH_TEXT";
    public String ENSAMBLE_ID = "ENSAMBLE_ID";
    public Boolean SEARCH_ACTION = false;
    public String SEARCH_ACTION_CODE = "SEARCH_CODE";
    private String SearchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ensambles);
        searchTextOption = findViewById(R.id.search_by_text);
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();
        AssembliesDao assembliesDao = database.assembliesDao();
        Stetho.initializeWithDefaults(this);

        cargarWebServiseAssembly();

        if (savedInstanceState != null){
            searchTextOption.setText(savedInstanceState.getString(SEARCH_TEXT));
            SEARCH_ACTION = savedInstanceState.getBoolean(SEARCH_ACTION_CODE);
            if (SEARCH_ACTION){
                if (searchTextOption.getText().toString().isEmpty()){
                    assemblies = assembliesDao.getAllAssemblies();
                    if (assembliesProducts != null && assembliesTotalCost != null){
                        assembliesProducts.clear();
                        assembliesTotalCost.clear();
                    }
                }
                else {
                    assemblies = assembliesDao.getAllAssembliesByText(searchTextOption.getText().toString());
                    if (assembliesProducts != null && assembliesTotalCost != null){
                        assembliesProducts.clear();
                        assembliesTotalCost.clear();
                    }
                }

                for(Assemblies assembly: assemblies){
                    assembliesProducts.add(assembliesProductsDao.getNumberProductsById(assembly.getId()));
                    assembliesTotalCost.add(assembliesProductsDao.getCostByAssemblyID(assembly.getId()));
                }
                assembliesRecyclerView = findViewById(R.id.assemblies_RecyclerView);
                assembliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                assembliesRecyclerView.setAdapter(new AssembliesAdapter(assemblies,assembliesProducts,assembliesTotalCost,this,this));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assembliesRecyclerView.getContext(),
                        new LinearLayoutManager(this).getOrientation());
                assembliesRecyclerView.addItemDecoration(dividerItemDecoration);
            }
        }

        Toolbar assembliesToolbar = findViewById(R.id.toolbar_assemblies);
        setSupportActionBar(assembliesToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assemblies_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_search:{
                Toast.makeText(this,"Buscando...",Toast.LENGTH_SHORT).show();
                AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
                AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();
                AssembliesDao assembliesDao = database.assembliesDao();
                if (searchTextOption.getText().toString().isEmpty()){
                    assemblies = assembliesDao.getAllAssemblies();
                    if (assembliesProducts != null && assembliesTotalCost != null){
                        assembliesProducts.clear();
                        assembliesTotalCost.clear();
                    }
                }
                else {
                    assemblies = assembliesDao.getAllAssembliesByText(searchTextOption.getText().toString());
                    if (assembliesProducts != null && assembliesTotalCost != null){
                        assembliesProducts.clear();
                        assembliesTotalCost.clear();
                    }
                }

                if (assemblies == null){
                    Toast.makeText(this,"Sin resultados",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"Se han encontrado " + String.valueOf(assemblies.size()) + " similitudes",Toast.LENGTH_SHORT).show();
                }

                for(Assemblies assembly: assemblies){
                    assembliesProducts.add(assembliesProductsDao.getNumberProductsById(assembly.getId()));
                    assembliesTotalCost.add(assembliesProductsDao.getCostByAssemblyID(assembly.getId()));
                }
                SearchText = searchTextOption.getText().toString();
                assembliesRecyclerView = findViewById(R.id.assemblies_RecyclerView);
                assembliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                assembliesRecyclerView.setAdapter(new AssembliesAdapter(assemblies,assembliesProducts,assembliesTotalCost,this,this));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assembliesRecyclerView.getContext(),
                        new LinearLayoutManager(this).getOrientation());
                assembliesRecyclerView.addItemDecoration(dividerItemDecoration);
                SEARCH_ACTION = true;
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   // @Override
   // public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
   //     super.onCreateContextMenu(menu, v, menuInfo);
   //     getMenuInflater().inflate(R.menu.assemblies_menu,menu);
   // }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EnsamblesActivity.this, MainActivity.class);
        EnsamblesActivity.super.finish();
        startActivityForResult(intent, MainActivity.PRINCIPAL_REQUEST_CODE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_TEXT,SearchText);
        outState.putBoolean(SEARCH_ACTION_CODE,SEARCH_ACTION);
    }

    @Override
    public void OnAssemblyClick(int position) {
        Intent intent = new Intent(this,secondaryActivity_assemblies.class);
        intent.putExtra(ENSAMBLE_ID,assemblies.get(position).getId());
        startActivity(intent);
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

    private JsonArrayRequest getRequest;

    private void cargarWebServiseAssembly() {

        //Llamado a la base de datos para la utilizacion de Dao's
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        final AssembliesDao assembliesDao = database.assembliesDao();

//Actualizo assemblies
        request = Volley.newRequestQueue(EnsamblesActivity.this);
        request2 = Volley.newRequestQueue(EnsamblesActivity.this);
        String url =  "http://192.168.1.101:3000/assemblies/"  ;

        getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        jsonArray2 = response;

                        jsonObject = null;
                        try {
                            assembliesDao.DeleteAssembliesTable();
                            for (int i = 0; i <= jsonArray2.length(); i++) {
                                jsonObject = jsonArray2.getJSONObject(i);
                                id_assemblies = jsonObject.getInt("id");
                                description_assemblies = jsonObject.getString("description");

                                //assembliesRemoteDatabase.add(new Assemblies( id_assemblies , description_assemblies ));

                                //ACTULIZACION DE LA TABLA
                                assembliesDao.InsertAssemblies(new Assemblies(id_assemblies, description_assemblies));
                            }
                            Toast.makeText(EnsamblesActivity.this, "ASSEMBLIES", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        assembliesRemoteDatabase2 = assembliesRemoteDatabase;
//AQUI DEBERIAMOS REALIZAR LA ACTUALIZACION DE LA BASE DE DATOS
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EnsamblesActivity.this, error.toString() + "FUCK", Toast.LENGTH_LONG).show();
                    }
                }
        );
        request2.add(getRequest);
    }
}
