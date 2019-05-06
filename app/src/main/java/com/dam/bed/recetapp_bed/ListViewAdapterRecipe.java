package com.dam.bed.recetapp_bed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapterRecipe extends BaseAdapter{
    Context mContext;
    LayoutInflater inflater;
    List<Recipe> recipeList;
    ArrayList<Recipe> arrayList;

    public ListViewAdapterRecipe(Context context, List<Recipe> recipeList) {
        mContext = context;
        this.recipeList = recipeList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Recipe>();
        this.arrayList.addAll(recipeList);
    }

    public class ViewHolder{
        TextView titleRecipe, durationRecipe;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row_recipe, null);

            holder.titleRecipe = convertView.findViewById(R.id.titleRecipe);
            holder.durationRecipe = convertView.findViewById(R.id.durationRecipe);
            holder.iconRecipe = convertView.findViewById(R.id.recipeIcon);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.titleRecipe.setText(recipeList.get(position).getName());
        holder.durationRecipe.setText(recipeList.get(position).getDietType());
        holder.iconRecipe.setImageResource(recipeList.get(position).getImage());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO on click go to recipe
            }
        });
        return convertView;
    }
}
