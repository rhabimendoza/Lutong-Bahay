package com.example.lutongbahay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    RecyclerView recycler_view;
    SearchView search_view;
    ImageView img_filter;
    GridLayoutManager grid_lay;
    Bundle b;
    String budget, difficulty, ingredient, flavor;
    ProgressDialog prog_dial;
    DatabaseReference data_ref;
    ValueEventListener event_list;
    Intent i;
    DataClass data;
    DataAdapter adapter;
    List<DataClass> all, recipe;
    ArrayList<DataClass> s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Access resources
        recycler_view = findViewById(R.id.recycler_view);
        search_view = findViewById(R.id.search_view);
        img_filter = findViewById(R.id.img_filter);

        // Clear focus
        search_view.clearFocus();

        // Setup layout
        grid_lay = new GridLayoutManager(ListActivity.this,1);
        recycler_view.setLayoutManager(grid_lay);

        // Create arraylist
        all = new ArrayList<>();
        recipe = new ArrayList<>();

        // Set adapter to recycler view
        adapter = new DataAdapter(ListActivity.this, recipe);
        recycler_view.setAdapter(adapter);

        // Get all extras from intent
        b = getIntent().getExtras();
        if(b != null){
            budget = b.getString("Budget");
            difficulty = b.getString("Difficulty");
            ingredient = b.getString("Ingredient");
            flavor = b.getString("Flavor");
        }

        // Create and show progress dialog
        prog_dial = ProgressDialog.show(ListActivity.this, "Loading", "Please wait...", true);
        prog_dial.show();

        // Get database reference
        data_ref = FirebaseDatabase.getInstance().getReference("Recipe");

        event_list = data_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Clear lists
                all.clear();
                recipe.clear();

                // Add all recipe to all
                for(DataSnapshot item: snapshot.getChildren()){
                    data = item.getValue(DataClass.class);
                    all.add(data);
                }

                // SEQUENTIAL SEARCH ALGORITHM
                int a = 0, b = 0;
                for(DataClass d: all){
                    a = Integer.parseInt(d.getBudget());
                    b = Integer.parseInt(budget);
                    if(a <= b && d.getDifficulty().equals(difficulty)){
                        if(!ingredient.equals("") && !flavor.equals("")){
                            if(d.getIngredient().equals(ingredient) && d.getFlavor().equals(flavor)){
                                recipe.add(d);
                            }
                        }
                        else{
                            recipe.add(d);
                        }
                    }
                }

                // Use knapsack algorithm to show list of recipes
                knapsackAlgo(recipe, Integer.parseInt(budget));

                // Use quicksort algorithm to sort the list of recipes
                quicksortAlgo(recipe);

                // Notify data change and dismiss dialog
                adapter.notifyDataSetChanged();
                prog_dial.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                prog_dial.dismiss();
            }
        });

        // Call search recipe when text is changed
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecipe(newText);
                return true;
            }
        });

        // Direct to filter activity when filter image icon is clicked
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(ListActivity.this, FilterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void searchRecipe(String text){

        // Create recipe list based on typed text in search
        s = new ArrayList<>();
        for(DataClass data: recipe){
            if(data.getName().toLowerCase().contains(text.toLowerCase())){
                s.add(data);
            }
            adapter.searchRecipe(s);
        }
    }

    // KNAPSACK ALGORITHM
    public void knapsackAlgo(List<DataClass> recipe, int budget){
        int n = recipe.size();
        int[][] arr = new int[n + 1][budget + 1];

        for(int ctr = 1; ctr <= n; ctr++) {
            DataClass current = recipe.get(ctr - 1);
            int curBud = Integer.parseInt(current.getBudget());
            int curServe = Integer.parseInt(current.getServings());

            for(int ctr2 = 1; ctr2 <= budget; ctr2++) {

                if(curBud <= ctr2) {
                    arr[ctr][ctr2] = Math.max(curServe + arr[ctr - 1][ctr2 - curBud], arr[ctr - 1][ctr2]);
                }
                else {
                    arr[ctr][ctr2] = arr[ctr - 1][ctr2];
                }
            }
        }

        List<DataClass> selected = new ArrayList<>();
        int b = budget;
        for(int ctr = n; ctr > 0 && b > 0; ctr--) {
            if(arr[ctr][b] != arr[ctr - 1][b]) {
                DataClass rec = recipe.get(ctr - 1);
                selected.add(rec);
                b -= Integer.parseInt(rec.getBudget());
            }
        }

        recipe.clear();
        recipe.addAll(selected);
    }

    // QUICKSORT ALGORITHM
    public void quicksortAlgo(List<DataClass> recipe){
        quicksort(recipe, 0, recipe.size() - 1);
    }
    public void quicksort(List<DataClass> recipe, int low, int high){
        if(low < high){
            int index = partition(recipe, low, high);
            quicksort(recipe, low, index - 1);
            quicksort(recipe, index + 1, high);
        }
    }
    public int partition(List<DataClass> recipe, int low, int high){
        DataClass index = recipe.get(high);
        String name = index.getName();
        int ctr = low - 1;

        for(int ctr2 = low; ctr2 < high; ctr2++){
            DataClass rec = recipe.get(ctr2);
            String recName = rec.getName();

            if(recName.compareToIgnoreCase(name) <= 0){
                ctr++;
                Collections.swap(recipe, ctr, ctr2);
            }
        }
        Collections.swap(recipe, ctr + 1, high);
        return ctr + 1;
    }
}