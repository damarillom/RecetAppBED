package com.dam.bed.recetapp_bed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

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


        Recipe rec1 = new Recipe("Carne", "N o use", "Carne",
                new ArrayList<String>(), R.drawable.quinoas);
        Recipe rec2 = new Recipe("Cosa 2", "N sdfsdfsdfsdfe", "Vegetal", new ArrayList<String>(),R.drawable.quinoas);
        arrayList.add(rec1);
        arrayList.add(rec2);

        adapter = new ListViewAdapterRecipe(this, arrayList);
        listView.setAdapter(adapter);
    }
}
