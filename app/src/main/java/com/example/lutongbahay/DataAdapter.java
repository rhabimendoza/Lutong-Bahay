package com.example.lutongbahay;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {
    Context c;
    View v;
    Intent i;
    List <DataClass> recipe;

    public DataAdapter(Context c, List<DataClass> recipe){
        this.c = c;
        this.recipe = recipe;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Setup view of item in recyclerview
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new DataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {

        // Set contents to items in recycler view
        Glide.with(c).load(recipe.get(position).getImage_direc()).into(holder.list_image);
        holder.list_name.setText(recipe.get(position).getName());
        holder.list_creator.setText(recipe.get(position).getCreator());

        // Sends data that will be used in recipe activity when an item is selected
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Starts intent and sends data
                i = new Intent(c, RecipeActivity.class);
                i.putExtra("Name", recipe.get(holder.getAdapterPosition()).getName());
                i.putExtra("Budget", recipe.get(holder.getAdapterPosition()).getBudget());
                i.putExtra("Difficulty", recipe.get(holder.getAdapterPosition()).getDifficulty());
                i.putExtra("Ingredient", recipe.get(holder.getAdapterPosition()).getIngredient());
                i.putExtra("Procedure", recipe.get(holder.getAdapterPosition()).getProcedure());
                i.putExtra("Flavor", recipe.get(holder.getAdapterPosition()).getFlavor());
                i.putExtra("Servings", recipe.get(holder.getAdapterPosition()).getServings());
                i.putExtra("Creator", recipe.get(holder.getAdapterPosition()).getCreator());
                i.putExtra("Image", recipe.get(holder.getAdapterPosition()).getImage_direc());
                c.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipe.size();
    }

    public void searchRecipe(ArrayList<DataClass> s) {
        recipe = s;
        notifyDataSetChanged();
    }
}

class DataViewHolder extends RecyclerView.ViewHolder {
    ImageView list_image;
    TextView list_name, list_creator;
    CardView card_view;

    public DataViewHolder(@NonNull View v){
        super(v);

        // Access resources
        list_image = v.findViewById(R.id.list_image);
        list_name = v.findViewById(R.id.list_name);
        list_creator = v.findViewById(R.id.list_creator);
        card_view = v.findViewById(R.id.card_view);
    }
}