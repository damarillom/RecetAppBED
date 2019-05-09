package com.dam.bed.recetapp_bed;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
//    Button accept;

    ArrayList<String> ingredients = new ArrayList<>();
    ArrayList<String> ingredientsNo = new ArrayList<>();
    static ArrayAdapter<String> adapter;
    static ArrayAdapter<String> adapter2;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Ingredients");
    DatabaseReference userIngredients;

    private FirebaseAuth mAuth;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    String email, replacedEmail;
    String diet = "";
    final int OMNIV = 2;
    final int VEGETARIAN = 1;
    final int VEGAN = 0;

    // Conseguir dieta usuario
    // Filtrar ingredientes
    // Omniv = 2
    // Vegetarian = 1
    // Vegan = 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ingredients);

        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        replacedEmail = email.replace("@", "\\").
                replace(".", "-");
        userIngredients = database.getReference("users/" + replacedEmail + "/ingredients");
        final DatabaseReference userListener = database.getReference("users/" + replacedEmail);

        // Cambiar el titulo de la toolbar
        getSupportActionBar().setTitle(R.string.excludeIngredients);

        //View elements
        mListView = findViewById(R.id.listView);
        mListView2 = findViewById(R.id.listView2);
//        accept = findViewById(R.id.acceptIngredients);

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

        userListener.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                diet = user.getDiet();

                // Conseguir los ingredientes
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        System.out.println("Añadiendo ingredients a lista 1");

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            Ingredient ingredient = ds.getValue(Ingredient.class);

                            // FILTRAR INGREDIENTES POR EL TIPO DE DIETA DEL USUARIO
                            if (diet.equalsIgnoreCase("Omniv")) {
                                //
                                if (!ingredientsNo.contains(ingredient.getName())) {
                                    ingredients.add(ingredient.getName());
                                }
                            }
                            else if (diet.equalsIgnoreCase("Vegetarian")) {
                                // Coge el 0 y el 1
                                if (ingredient.getType() == VEGAN || ingredient.getType() == VEGETARIAN) {

                                    if (!ingredientsNo.contains(ingredient.getName())) {
                                        ingredients.add(ingredient.getName());
                                    }
                                }
                            }
                            else if (diet.equalsIgnoreCase("Vegan")) {
                                if (ingredient.getType() == VEGAN) {    // Coge el 0

                                    if (!ingredientsNo.contains(ingredient.getName())) {
                                        ingredients.add(ingredient.getName());
                                    }
                                }
                            }

                            System.out.println("ingredient - " + ingredient);

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userIngredients.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("Añadiendo ingredients a lista 2");
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




        //Onclick de la lista principal
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = mListView.getItemAtPosition(position);
                System.out.println("position - " + position);
                String item = (String) o;
//                Snackbar.make(view, "Adding ingredient: "+item, Snackbar.LENGTH_SHORT).show();

                // Que no haya duplicados
                if (ingredientsNo.contains(item)) return;

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

                // Que no haya duplicados
                if (ingredients.contains(item)) return;

                ingredients.add(item);          // Añadir a la primera lista
                Collections.sort(ingredients);  // Ordenar la lista
                System.out.println("adding ingredient " + item + " a lista 1");
                adapter.notifyDataSetChanged();

                // Eliminar de la lista secundaria
                ingredientsNo.remove(item);
                adapter2.notifyDataSetChanged();
            }
        });


//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("Lista de ingredientes que se excluyen");
//                saveIngredients(ingredientsNo);
//            }
//        });

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

        startActivity(new Intent(SelectIngredients.this, RecipeList.class));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ingredients_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setQueryHint(getText(R.string.searchingredient));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //se oculta el EditText
//                searchView.setQuery("", true);
//                searchView.setIconified(true);
//                return true;
//            }
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (TextUtils.isEmpty(newText)) {
//                    mListView.clearTextFilter();
//                } else {
//                    mListView.setFilterText(newText);
//                }
//                return true;
//            }
//        });
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                searchView.clearFocus();
//                return true;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onStart() {
        super.onStart();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                System.out.println("Lista de ingredientes que se excluyen");
                saveIngredients(ingredientsNo);

                switch (menuItem.getItemId()) {
                    case R.id.action_recipe:
                        startActivity(new Intent(getBaseContext(), RecipeList.class));
                        break;

                    case R.id.action_ingredient:
                        startActivity(new Intent(getBaseContext(), SelectIngredients.class));
                        break;

                    case R.id.action_cuest:
                        startActivity(new Intent(getBaseContext(), Cuestionario.class));
                        break;
                }
                return true;
            }
        });
    }
}

