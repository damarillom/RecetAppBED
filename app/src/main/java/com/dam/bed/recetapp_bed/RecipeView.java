package com.dam.bed.recetapp_bed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeView extends AppCompatActivity {

    static String ingredientes = "";
    static TextView title, ingredients, description;
    static ImageView image;
    final long ONE_MEGABYTE = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        image = (ImageView) findViewById(R.id.imageView2);
        title = (TextView) findViewById(R.id.title);
        ingredients = (TextView) findViewById(R.id.ingredients);
        description = (TextView) findViewById(R.id.description);
        String name = "Pollo con arroz";
        title.setText(name);
        ingredientes = "Ingredientes:\n";

        //PRUEBA STORAGE
        // Create a storage reference from our app
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference imageRef = storageRef.child("quinoas.jpg");

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("databaseError = " + databaseError);
            }
        };
        FirebaseDatabase.getInstance().getReference("Recipes/" + name).addValueEventListener(valueEventListener);
    }
}
