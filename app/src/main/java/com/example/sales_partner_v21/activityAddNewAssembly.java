package com.example.sales_partner_v21;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.sales_partner_v21.Database.AppDatabase;
import com.example.sales_partner_v21.Database.Assemblies;
import com.example.sales_partner_v21.Database.AssembliesDao;
import com.example.sales_partner_v21.Database.AssembliesProductsDao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

class AddNewAssemblyAdapter extends RecyclerView.Adapter<AddNewAssemblyAdapter.ViewHolder> {
    private List<Assemblies> assembliesList;
    private List<Integer> assembliesProductsList;
    private List<Integer> assembliesTotalCostList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView txtDescription;
        private TextView txtNumberProducts;
        private TextView txtTotalCost;

        private Assemblies assemblies;
        private int assembliesProducts;
        private int assembliesTotalCost;
        private Context context;

        public String ENSAMBLE_ID = "ENSAMBLE_ID";

        NumberFormat formatter = new DecimalFormat("#,###");

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtDescription = itemView.findViewById(R.id.description);
            txtNumberProducts = itemView.findViewById(R.id.number_products);
            txtTotalCost = itemView.findViewById(R.id.total_cost);
            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(Assemblies assemblies, int assembliesProducts, int assembliesTotalCost, Context context) {
            this.assemblies = assemblies;
            this.assembliesProducts = assembliesProducts;
            this.assembliesTotalCost = assembliesTotalCost;
            this.context = context;
            txtDescription.setText(assemblies.getDescription());
            txtNumberProducts.setText(String.valueOf(assembliesProducts));
            txtTotalCost.setText("$ " + formatter.format(assembliesTotalCost));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem details = menu.add(this.getAdapterPosition(), 0, 0, "Detalles");
            MenuItem add = menu.add(this.getAdapterPosition(), 1, 0, "Agregar");

            details.setOnMenuItemClickListener(onOptionMenu);
            add.setOnMenuItemClickListener(onOptionMenu);
        }

        private final MenuItem.OnMenuItemClickListener onOptionMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                if (item.getItemId() == 0) {
                    Intent detailsIntent = new Intent(itemView.getContext(), secondaryActivity_assemblies.class);
                    detailsIntent.putExtra(ENSAMBLE_ID, assemblies.getId());
                    itemView.getContext().startActivity(detailsIntent);
                    return true;
                }
                if (item.getItemId() == 1) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setMessage("¿Desea agregar el ensamble a la orden?").setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent addIntent = new Intent();
                            addIntent.putExtra(ENSAMBLE_ID, assemblies.getId());
                            ((activityAddNewAssembly) context).setResult(Activity.RESULT_OK, addIntent);
                            ((activityAddNewAssembly) context).finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return true;
                } else {
                    return true;
                }
            }
        };
    }

    public AddNewAssemblyAdapter(List<Assemblies> assembliesList,List<Integer> assembliesProductsList,List<Integer> assembliesTotalCostList, Context context){
        this.assembliesList = assembliesList;
        this.assembliesProductsList = assembliesProductsList;
        this.assembliesTotalCostList = assembliesTotalCostList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.assembly_item,viewGroup,false);
        ((Activity)viewGroup.getContext()).registerForContextMenu(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(assembliesList.get(position),assembliesProductsList.get(position),assembliesTotalCostList.get(position),context);
    }

    @Override
    public int getItemCount() {
        return assembliesList.size();
    }
}

public class activityAddNewAssembly extends AppCompatActivity{
    public static final int ENSAMBLES_REQUEST_CODE = 1;
    private RecyclerView assembliesRecyclerView;
    private EditText searchTextOption;
    List<Assemblies> assemblies = new ArrayList<>();
    List<Integer> assembliesProducts = new ArrayList<>();
    List<Integer> assembliesTotalCost = new ArrayList<>();
    public String SEARCH_TEXT = "SEARCH_TEXT";
    public String ENSAMBLE_ID = "ENSAMBLE_ID";
    public Boolean SEARCH_ACTION = false;
    public String SEARCH_ACTION_CODE = "SEARCH_CODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_assembly);

        searchTextOption = findViewById(R.id.search_by_text);
        AppDatabase database = AppDatabase.getAppDatabase(getApplicationContext());
        AssembliesProductsDao assembliesProductsDao = database.assembliesProductsDao();
        AssembliesDao assembliesDao = database.assembliesDao();

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
                    assert assembliesProducts != null;
                    assembliesProducts.add(assembliesProductsDao.getNumberProductsById(assembly.getId()));
                    assembliesTotalCost.add(assembliesProductsDao.getCostByAssemblyID(assembly.getId()));
                }
                assembliesRecyclerView = findViewById(R.id.assemblies_RecyclerView);
                assembliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                assembliesRecyclerView.setAdapter(new AddNewAssemblyAdapter(assemblies,assembliesProducts,assembliesTotalCost,this));
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(assembliesRecyclerView.getContext(),
                        new LinearLayoutManager(this).getOrientation());
                assembliesRecyclerView.addItemDecoration(dividerItemDecoration);
            }
        }

        Toolbar assembliesToolbar = findViewById(R.id.toolbar_assemblies);
        setSupportActionBar(assembliesToolbar);
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
                assembliesRecyclerView = findViewById(R.id.assemblies_RecyclerView);
                assembliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                assembliesRecyclerView.setAdapter(new AddNewAssemblyAdapter(assemblies,assembliesProducts,assembliesTotalCost,this));
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SEARCH_TEXT,searchTextOption.getText().toString());
        outState.putBoolean(SEARCH_ACTION_CODE,SEARCH_ACTION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assemblies_menu,menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.assemblies_menu,menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityAddNewAssembly.super.finish();
    }
}
