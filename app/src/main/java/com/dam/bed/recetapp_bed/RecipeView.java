package com.dam.bed.recetapp_bed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RecipeView extends AppCompatActivity {

    private FirebaseAuth mAuth;

    static String ingredientes = "";
    static TextView title, ingredients, description;
    static ImageView image;
    final long ONE_MEGABYTE = 1024 * 1024;
    static int textSize = 16;

    // Limites del size de las letras
    private final int MAX_TEXT_SIZE = 22;
    private final int MIN_TEXT_SIZE = 14;

    static String img = "";

    String dirPath = "/data/data/com.dam.bed.recetapp_bed/files/images/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view);

        mAuth = FirebaseAuth.getInstance();

        String email = mAuth.getCurrentUser().getEmail();
        String replacedEmail = SingletonRecetApp.getInstance().replaceEmail(email);

        ValueEventListener valueEventListenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {
                    if (user.getLetterSize() > 0) {
                        textSize = user.getLetterSize();
                        ingredients.setTextSize(textSize);
                        description.setTextSize(textSize);
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("databaseError = " + databaseError);
            }
        };
        FirebaseDatabase.getInstance().getReference("users/" + replacedEmail).addValueEventListener(valueEventListenerUser);

        image = (ImageView) findViewById(R.id.imageView2);
        title = (TextView) findViewById(R.id.title);
        ingredients = (TextView) findViewById(R.id.ingredients);
        description = (TextView) findViewById(R.id.description);

        String name = "Pollo con arroz";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("Name");
            System.out.println("*********************" + name);
        }


        // Nombre de la receta en la toolbar
        getSupportActionBar().setTitle(name);
        ingredientes = "Ingredientes:\n\n";


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Recipe recipe = dataSnapshot.getValue(Recipe.class);

                for(int x=0;x<recipe.getIngredients().size();x++) {
                    System.out.println(recipe.getIngredients().get(x));
                    ingredientes += "\t- " + recipe.getIngredients().get(x) + "\n";
                }

                ingredients.setText(ingredientes);

                description.setText("Pasos a seguir:\n\n" + recipe.getDescription());

                img = recipe.getImg();

                //STORAGE

                File photo = new File(dirPath + img);
                Bitmap bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), new BitmapFactory.Options());
                image.setImageBitmap(bitmap);
                /**StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                StorageReference imageRef = storageRef.child(img);

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
                });*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("databaseError = " + databaseError);
            }
        };
        FirebaseDatabase.getInstance().getReference("Recipes/" + name).addValueEventListener(valueEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.letter_size, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mas:

                changeTextSize(2);
                return true;

            case R.id.menos:

                changeTextSize(-2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Cambiar el size del texto de la receta
     * @param num
     * @return
     */
    private boolean changeTextSize(int num) {

        String email = mAuth.getCurrentUser().getEmail();
        String replacedEmail = SingletonRecetApp.getInstance().replaceEmail(email);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {
                    if (user.getLetterSize() > 0) {
                        textSize = user.getLetterSize();
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("databaseError = " + databaseError);
            }
        };
        FirebaseDatabase.getInstance().getReference("users/" + replacedEmail).addValueEventListener(valueEventListener);


        // Limites de size de las letras
        if (num > 0 && textSize == MAX_TEXT_SIZE) return false;
        if (num < 0 && textSize == MIN_TEXT_SIZE) return false;

        textSize += num;
        ingredients.setTextSize(textSize);
        description.setTextSize(textSize);
        System.out.println("Text size: " + textSize);

        Map<String, Object> datosActualizar = new HashMap<>();
        datosActualizar.put("letterSize", textSize);
        FirebaseDatabase.getInstance().getReference("users/" + replacedEmail).
                updateChildren(datosActualizar);
        return true;
    }

}
