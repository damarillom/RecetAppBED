package com.dam.bed.recetapp_bed;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.dam.bed.recetapp_bed.R.string.searchingredient;

public class SelectIngredients extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView mSearchView;
    ListView mListView, mListView2;
    ArrayList<String> ingredients = new ArrayList<>();
    ArrayList<String> ingredientsNo = new ArrayList<>();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ingredients");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ingredients);

        //View elements
        mSearchView = findViewById(R.id.searchView);
        mListView = findViewById(R.id.listView);
        mListView2 = findViewById(R.id.listView2);

        // Conseguir los ingredientes
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Ingredient ingredient = ds.getValue(Ingredient.class);
                    System.out.println("ingredient - " + ingredient);
                    ingredients.add(ingredient.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        // Añadir los ingredientes a la lista
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                ingredients);
        mListView.setAdapter(adapter);
        mListView.setBackgroundColor(Color.parseColor("#99ff99"));
        mListView.setTextFilterEnabled(true);

        // Lista 2 - Ingredientes que se excluyen
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,
                ingredientsNo);
        mListView2.setAdapter(adapter2);
        mListView2.setBackgroundColor(Color.parseColor("#ff5050"));

        setupSearchView();

        //Onclick de la lista principal
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = mListView.getItemAtPosition(position);
                String item = (String) o;
                Snackbar.make(view, "Adding ingredient: "+item, Snackbar.LENGTH_SHORT).show();

                // Añadir a la segunda lista
//                ingredientsNo.add(item);
                adapter2.add(item);
                adapter2.notifyDataSetChanged();

                // Eliminar de la lista principal
                adapter.remove(item);
                adapter.notifyDataSetChanged();
            }
        });

        // Onclick segunda lista
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = mListView2.getItemAtPosition(position);
                String item = (String) o;
                Snackbar.make(view, "Removing ingredient: "+item, Snackbar.LENGTH_SHORT).show();

                // Añadir a la primera lista
//                ingredientsNo.add(item);
                adapter.add(item);
                adapter.notifyDataSetChanged();

                // Eliminar de la lista secundaria
                adapter2.remove(item);
                adapter2.notifyDataSetChanged();
            }
        });
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
//        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search ingredient");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText);
        }
        return true;
    }

}

