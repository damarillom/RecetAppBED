package com.dam.bed.recetapp_bed;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecipeView extends AppCompatActivity {

    static String ingredientes = "";
    static TextView title, ingredients, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        title = (TextView) findViewById(R.id.title);
        ingredients = (TextView) findViewById(R.id.ingredients);
        description = (TextView) findViewById(R.id.description);
        String name = "Pollo con arroz";
        title.setText(name);
        ingredientes = "Ingredientes:\n";


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);
                System.out.println("************************"+recipe.getName());
                //System.out.println("*********"+user.getAltura());
                for(int x=0;x<recipe.getIngredients().size();x++) {
                    System.out.println(recipe.getIngredients().get(x));
                    ingredientes += "\t- " + recipe.getIngredients().get(x) + "\n";
                }

                ingredients.setText(ingredientes);

                description.setText(recipe.getDescription());
                /**if (user.isQuest()) {
                    editTextHeight.setText(Double.toString(user.getAltura()));
                    editTextWeight.setText(Double.toString(user.getPeso()));
                    editTextYear.setText(Integer.toString(user.getBirthday()));
                    String gender = user.getGender();
                    String diet = user.getDiet();
                    RadioButton radioM = (RadioButton) findViewById(R.id.radioMale);
                    RadioButton radioF = (RadioButton) findViewById(R.id.radioFemale);
                    RadioButton radioO = (RadioButton) findViewById(R.id.radioOther);
                    RadioButton radioVegano = (RadioButton) findViewById(R.id.radioVegan);
                    RadioButton radioVegetarian = (RadioButton) findViewById(R.id.radioVegetarian);
                    RadioButton radioOmnivore = (RadioButton) findViewById(R.id.radioOmnivore);
                    if (gender.equals("M")) {
                        radioM.setChecked(true);
                    } else if (gender.equals("F")) {
                        radioF.setChecked(true);
                    } else if (gender.equals("O")) {
                        radioO.setChecked(true);
                    }
                    if (diet.equals("Omniv")) {
                        radioOmnivore.setChecked(true);
                    } else if (diet.equals("Vegetarian")) {
                        radioVegetarian.setChecked(true);
                    } else if (diet.equals("Vegan")) {
                        radioVegano.setChecked(true);
                    }
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("databaseError = " + databaseError);
            }
        };
        FirebaseDatabase.getInstance().getReference("Recipes/" + name).addValueEventListener(valueEventListener);




        /**ingredients += "\t- patata\n";
        ingredients += "\t2. Quinoa\n";
        ingredients += "\t1. Salmon\n";
        ingredients += "\t1. Espinaca\n";
        ingredients += "\t2. Quinoa\n";
        ingredients += "\t1. Salmon\n";
        ingredients += "\t1. Espinaca\n";
        ingredients += "\t1. patata\n";
        ingredients += "\t2. Quinoa\n";
        ingredients += "\t1. Salmon\n";
        ingredients += "\t1. Espinaca\n";
        ingredients += "\t2. Quinoa\n";
        ingredients += "\t1. Salmon\n";
        ingredients += "\t1. Espinaca\n";*/

        /**description.setText("Lorem ipsum dolor sit amet consectetur adipiscing elit, auctor nulla facilisis sed neque nullam, netus senectus nunc duis tempus per. Nibh elementum feugiat pharetra per dictumst nascetur id viverra, integer consequat mauris venenatis malesuada hac nulla. Metus dignissim vehicula posuere lacinia donec taciti ridiculus eros, per rhoncus viverra tempor pretium arcu integer euismod, lectus id penatibus orci montes nisl eleifend.\n" +
                "\n" +
                "Inceptos in curabitur malesuada a sollicitudin per varius tellus nisl, torquent laoreet lectus gravida fusce scelerisque turpis vehicula, nostra parturient porta bibendum accumsan placerat aptent ad. Ultrices penatibus velit viverra metus massa fringilla praesent mi, sodales eu consequat nibh egestas ut mattis hendrerit aliquet, sed pretium ridiculus integer magna gravida potenti. Nisi metus nunc curae vivamus maecenas himenaeos fames feugiat accumsan, auctor parturient libero montes venenatis aptent erat et eros augue, diam viverra nisl faucibus non praesent potenti eget.");*/

    }
}
