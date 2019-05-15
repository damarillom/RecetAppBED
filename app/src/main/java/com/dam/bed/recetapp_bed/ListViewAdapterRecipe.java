package com.dam.bed.recetapp_bed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapterRecipe extends BaseAdapter{
    Context mContext;
    LayoutInflater inflater;
    List<Recipe> recipeList;
    ArrayList<Recipe> arrayList;

    final long ONE_MEGABYTE = 1024 * 1024;
    ImageView image;
    String nameRecipe;

    String imagePath = "";
    String dirPath = "/data/data/com.dam.bed.recetapp_bed/files/images/";


    public ListViewAdapterRecipe(Context context, List<Recipe> recipeList) {
        mContext = context;
        this.recipeList = recipeList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Recipe>();
        this.arrayList.addAll(recipeList);
    }

    public class ViewHolder{
        TextView titleRecipe, typeRecipe;
        ImageView iconRecipe;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_recipe, null);

            holder.titleRecipe = convertView.findViewById(R.id.titleRecipe);
            holder.typeRecipe = convertView.findViewById(R.id.typeRecipe);
            holder.iconRecipe = convertView.findViewById(R.id.recipeIcon);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleRecipe.setText(recipeList.get(position).getName());
        holder.typeRecipe.setText(recipeList.get(position).getType());

        //
        imagePath = recipeList.get(position).getImg();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference imageRef = storageRef.child(imagePath);
        File f = new File(dirPath + imagePath);
        System.out.println();
        if (!f.exists()) {
            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(byte[] bytes) {
                    imagePath = recipeList.get(position).getImg();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    //String root = Environment.getExternalStorageDirectory().getAbsolutePath();
                    File myDir = new File(dirPath);
                    myDir.mkdir();
                    //System.out.println(myDir.exists() + "*********************************");
                    File file = new File(myDir, imagePath);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (IOException e) {
                        System.out.println("ERROR: " + e);
                        e.printStackTrace();
                    }


                    holder.iconRecipe.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } else {
            Bitmap b = BitmapFactory.decodeFile(f.getAbsolutePath(), new BitmapFactory.Options());
            holder.iconRecipe.setImageBitmap(b);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext.getApplicationContext(), RecipeView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Name", holder.titleRecipe.getText().toString());
//                System.out.println("*********" + holder.titleRecipe.getText().toString());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
