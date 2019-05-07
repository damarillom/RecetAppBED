package com.dam.bed.recetapp_bed;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeList extends AppCompatActivity {

    ListView listView;
    ListViewAdapterRecipe adapter;

    ArrayList<Recipe> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        listView = findViewById(R.id.listViewRecipeList);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Recipe recipe = ds.getValue(Recipe.class);
//                    System.out.println("recipe = " + recipe);
                    arrayList.add(recipe);
                }

//                System.out.println("arrayList Recipes****************************= " + arrayList);
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
}
