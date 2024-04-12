package com.example.lutongbahay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {
    EditText txt_budget;
    Spinner spinner_difficulty;
    CheckBox cb_customize;
    RadioGroup rg_ingredient, rg_flavor;
    Button btn_filter;
    ArrayAdapter<CharSequence> adapter_difficulty;
    ConnectivityManager cm;
    NetworkInfo ni;
    String budget, difficulty, ingredient, flavor;
    int id_ing, id_fla;
    RadioButton sel_ing, sel_fla;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Access resources
        txt_budget = findViewById(R.id.txt_budget);
        spinner_difficulty = findViewById(R.id.spinner_difficulty);
        cb_customize = findViewById(R.id.cb_customize);
        rg_ingredient = findViewById(R.id.rg_ingredient);
        rg_flavor = findViewById(R.id.rg_flavor);
        btn_filter = findViewById(R.id.btn_filter);

        // Add contents to spinners
        setupSpinner();

        // Set all radiobutton to false by default
        setRadioGroupState(rg_ingredient, false);
        setRadioGroupState(rg_flavor, false);

        // Set the first options as selected
        rg_ingredient.check(rg_ingredient.getChildAt(0).getId());
        rg_flavor.check(rg_flavor.getChildAt(0).getId());

        // Enable radiobuttons if the customize checkbox is checked
        cb_customize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // Enable radiobuttons
                if(isChecked){
                    setRadioGroupState(rg_ingredient, true);
                    setRadioGroupState(rg_flavor, true);
                }

                // Disable radiobuttons
                else{
                    setRadioGroupState(rg_ingredient, false);
                    setRadioGroupState(rg_flavor, false);
                }
            }
        });

        // Get all selected filter specifications when button is clicked
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get selected budget and difficulty
                budget = txt_budget.getText().toString();

                // Check if user typed a recipe budget
                if(budget.isEmpty()){
                    Toast.makeText(FilterActivity.this, "Input food budget!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get selected difficulty
                difficulty = spinner_difficulty.getSelectedItem().toString();

                // Check if customize is checked
                if(cb_customize.isChecked()){

                    // Get selected ingredient and flavor
                    id_ing = rg_ingredient.getCheckedRadioButtonId();
                    id_fla = rg_flavor.getCheckedRadioButtonId();
                    sel_ing = rg_ingredient.findViewById(id_ing);
                    sel_fla = rg_flavor.findViewById(id_fla);
                    ingredient = sel_ing.getText().toString();
                    flavor = sel_fla.getText().toString();
                }

                // Customize is not checked
                else{

                    // Set ingredient and flavor to blank
                    ingredient = "";
                    flavor = "";
                }

                // Check if it is connected to network
                if(!isConnected()){
                    Toast.makeText(FilterActivity.this, "No network connection!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Start new intent and put extra to filter recipes
                i = new Intent(FilterActivity.this, ListActivity.class);
                i.putExtra("Budget", budget);
                i.putExtra("Difficulty", difficulty);
                i.putExtra("Ingredient", ingredient);
                i.putExtra("Flavor", flavor);
                startActivity(i);
                finish();
            }
        });
    }

    public void setupSpinner(){

        // Create adapters
        adapter_difficulty = ArrayAdapter.createFromResource(this, R.array.difficulty_range, android.R.layout.simple_spinner_item);

        //Fix layout
        adapter_difficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapters to the spinners
        spinner_difficulty.setAdapter(adapter_difficulty);
    }

    public void setRadioGroupState(RadioGroup rg, boolean state){

        // Change state of radiogroup child
        for(int ctr = 0; ctr < rg.getChildCount(); ctr++){
            rg.getChildAt(ctr).setEnabled(state);
        }
    }

    public boolean isConnected(){

        // Check if the device is connected to wifi or mobile data
        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        return ni != null && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE) && ni.isConnected();
    }
}