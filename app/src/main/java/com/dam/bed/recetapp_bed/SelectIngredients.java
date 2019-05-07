package com.dam.bed.recetapp_bed;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.support.v7.widget.MenuItemHoverListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static com.dam.bed.recetapp_bed.R.string.searchingredient;

public class SelectIngredients extends AppCompatActivity {

//    SearchView mSearchView;
    ListView mListView, mListView2;
    Button accept;

    ArrayList<String> ingredients = new ArrayList<>();
    ArrayList<String> ingredientsNo = new ArrayList<>();
    static ArrayAdapter<String> adapter;
    static ArrayAdapter<String> adapter2;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ingredients");

    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    String email;
    String replacedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ingredients);

        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        replacedEmail = email.replace("@", "\\").
                replace(".", "-");
        DatabaseReference userIngredients = database.getReference("users/" + replacedEmail + "/ingredients");

        //View elements
//        mSearchView = findViewById(R.id.action_search);
        mListView = findViewById(R.id.listView);
        mListView2 = findViewById(R.id.listView2);
        accept = findViewById(R.id.acceptIngredients);

        // Añadir los ingredientes a la lista
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                ingredients);

        // Lista 2 - Ingredientes que se excluyen
        adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_expandable_list_item_1,
                ingredientsNo);

        mListView.setAdapter(adapter);
        mListView.setBackgroundColor(Color.parseColor("#99ff99"));
        mListView.setTextFilterEnabled(true);
        mListView.setItemsCanFocus(true);

        mListView2.setAdapter(adapter2);
        mListView2.setBackgroundColor(Color.parseColor("#ff5050"));

        userIngredients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("Añadiento ingredients a lista 2");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String ingredient = ds.getValue(String.class);
                    ingredientsNo.add(ingredient);
                    System.out.println("ingredient - " + ingredient);
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Conseguir los ingredientes
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("Añadiento ingredients a lista 1");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Ingredient ingredient = ds.getValue(Ingredient.class);
                    if (!ingredientsNo.contains(ingredient.getName())) {
                        ingredients.add(ingredient.getName());
                    }
//                    contentIngredients.add(ingredient);
                    System.out.println("ingredient - " + ingredient);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });




        //Onclick de la lista principal
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = mListView.getItemAtPosition(position);
                System.out.println("position - " + position);
                String item = (String) o;
//                Snackbar.make(view, "Adding ingredient: "+item, Snackbar.LENGTH_SHORT).show();

                // Añadir a la segunda lista
                ingredientsNo.add(item);
                Collections.sort(ingredientsNo);    // Ordernar lista 2
                System.out.println("adding ingredient " + item + " a lista 2");
                adapter2.notifyDataSetChanged();

                // Eliminar de la lista principal
//                adapter.remove(item);
                ingredients.remove(item);
                adapter.notifyDataSetChanged();

//                mSearchView.clearFocus();
//                mListView.clearTextFilter();
            }
        });

        // Onclick segunda lista
        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = mListView2.getItemAtPosition(position);
                String item = (String) o;
//                Snackbar.make(view, "Removing ingredient: "+item, Snackbar.LENGTH_SHORT).show();


                ingredients.add(item);          // Añadir a la primera lista
                Collections.sort(ingredients);  // Ordenar la lista
                System.out.println("adding ingredient " + item + " a lista 1");
                adapter.notifyDataSetChanged();

                // Eliminar de la lista secundaria
                ingredientsNo.remove(item);
                adapter2.notifyDataSetChanged();
            }
        });

//        setupSearchView();

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Lista de ingredientes que se excluyen");
                saveIngredients(ingredientsNo);
            }
        });

    }

    /**
     * Guarda en la base de datos los ingredients que el usuario no quiere
     * @param ingredients
     */
    private void saveIngredients(ArrayList<String> ingredients) {

        for (String s : ingredients) {
            System.out.println(s);
        }


        Map<String, Object> ingredientsMap = new HashMap<>();
        ingredientsMap.put("ingredients", ingredients);

        FirebaseDatabase.getInstance().getReference("users/" + replacedEmail).
                updateChildren(ingredientsMap);

        Toast.makeText(this, R.string.added_ingredients, Toast.LENGTH_SHORT).show();
    }


//        private void setupSearchView() {
//        mSearchView.setIconifiedByDefault(false);
//        mSearchView.setOnQueryTextListener(this);
////        mSearchView.setSubmitButtonEnabled(true);
//        mSearchView.setQueryHint("Search ingredient");
////        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
////            @Override
////            public boolean onClose() {
////                mSearchView.clearFocus();
////                return true;
////            }
////        });
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ingredients_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getText(R.string.searchingredient));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    mListView.clearTextFilter();
                } else {
                    mListView.setFilterText(newText);
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                Toast.makeText(SelectIngredients.this, "", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}

