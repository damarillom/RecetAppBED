package com.dam.bed.recetapp_bed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
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
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference imageRef = storageRef.child(recipeList.get(position).getImg());

        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.iconRecipe.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext.getApplicationContext(), RecipeView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(holder.titleRecipe.getText().toString(),"Name");
//                System.out.println("*********" + holder.titleRecipe.getText().toString());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
