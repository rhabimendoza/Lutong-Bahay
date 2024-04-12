package com.example.lutongbahay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class RecipeActivity extends AppCompatActivity {
    TextView display_name, display_budget, display_difficulty, display_ingredient, display_flavor, display_servings, display_creator;
    ImageView display_image;
    EditText display_procedure;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Access resources
        display_name = findViewById(R.id.display_name);
        display_image = findViewById(R.id.display_image);
        display_budget = findViewById(R.id.display_budget);
        display_difficulty = findViewById(R.id.display_difficulty);
        display_ingredient = findViewById(R.id.display_ingredient);
        display_flavor = findViewById(R.id.display_flavor);
        display_servings = findViewById(R.id.display_servings);
        display_procedure = findViewById(R.id.display_procedure);
        display_creator = findViewById(R.id.display_creator);

        // Get all extras from intent
        b = getIntent().getExtras();
        if(b != null){
            display_name.setText(b.getString("Name"));
            display_budget.setText(b.getString("Budget"));
            display_difficulty.setText(b.getString("Difficulty"));
            display_ingredient.setText(b.getString("Ingredient"));
            display_flavor.setText(b.getString("Flavor"));
            display_servings.setText(b.getString("Servings"));
            display_procedure.setText(b.getString("Procedure"));
            display_creator.setText(b.getString("Creator"));
            Glide.with(this).load(b.getString("Image")).into(display_image);
        }
    }
}