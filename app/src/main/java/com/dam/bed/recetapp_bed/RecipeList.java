package com.dam.bed.recetapp_bed;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeList extends AppCompatActivity {

    ListView listView;
    ListViewAdapterRecipe adapter;

    ArrayList<Recipe> arrayList = new ArrayList<>();

    String diet = "";
    String email, replacedEmail;
    ArrayList<String> ingreUser, ingreRecipe;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        listView = findViewById(R.id.listViewRecipeList);

        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        replacedEmail = email.replace("@", "\\").
                replace(".", "-");

        ValueEventListener valueEventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                diet = user.getDiet();

                ingreUser = user.getIngredients();

                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            Recipe recipe = ds.getValue(Recipe.class);
                            ingreRecipe = recipe.getIngredients();

                            // Si la lista de ingredientes del usuario esta vacia,
                            // inicializamos ingreUser como una nueva ArrayLista para que no de null
                            if (ingreUser == null) ingreUser = new ArrayList<>();

                            if (Collections.disjoint(ingreUser, ingreRecipe)) {
                                if (diet.equals("Omniv")) {
                                    arrayList.add(recipe);
                                } else if (diet.equals("Vegetarian")) {
                                    if (!recipe.getType().equals("Omniv")) {
                                        arrayList.add(recipe);
                                    }
                                } else if (diet.equals("Vegan")) {
                                    if (recipe.getType().equals("Vegan")) {
                                        arrayList.add(recipe);
                                    }
                                }
                            }
                        }
                        adapter = new ListViewAdapterRecipe(getBaseContext(), arrayList);
                        listView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("error" + databaseError.getMessage());
                    }
                };
                FirebaseDatabase.getInstance().getReference("Recipes/").addValueEventListener(valueEventListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("error" + databaseError.getMessage());
            }
        };
        FirebaseDatabase.getInstance().getReference("users/" + replacedEmail).addValueEventListener(valueEventListenerUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            mAuth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

