package com.example.lutongbahay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResult;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InsertActivity extends AppCompatActivity {
    ImageView recipe_image;
    EditText recipe_name, recipe_procedure, recipe_creator, recipe_budget, recipe_servings;
    Spinner recipe_difficulty, recipe_ingredient, recipe_flavor;
    Button recipe_share;
    ArrayAdapter<CharSequence> adapter_difficulty, adapter_ingredient, adapter_flavor;
    Intent i;
    Uri uri, url;
    ConnectivityManager cm;
    NetworkInfo ni;
    ProgressDialog prog_dial;
    StorageReference store_ref;
    Task<Uri> uri_task;
    String image_direc;
    DataClass data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        // Access resources
        recipe_image = findViewById(R.id.recipe_image);
        recipe_name = findViewById(R.id.recipe_name);
        recipe_budget = findViewById(R.id.recipe_budget);
        recipe_difficulty = findViewById(R.id.recipe_difficulty);
        recipe_ingredient = findViewById(R.id.recipe_ingredient);
        recipe_flavor = findViewById(R.id.recipe_flavor);
        recipe_servings = findViewById(R.id.recipe_servings);
        recipe_procedure = findViewById(R.id.recipe_procedure);
        recipe_creator = findViewById(R.id.recipe_creator);
        recipe_share = findViewById(R.id.recipe_share);

        // Add contents to spinners
        setupSpinnerRecipe();

        // Change image in the view
        ActivityResultLauncher<Intent> act_launch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    i = result.getData();
                    uri = i.getData();
                    recipe_image.setImageURI(uri);
                }
                else{
                    Toast.makeText(InsertActivity.this, "No image selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Launch select image
        recipe_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                act_launch.launch(i);
            }
        });

        // Save all information to firebase
        recipe_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformation();
            }
        });
    }

    public void setupSpinnerRecipe(){

        // Create adapters
        adapter_difficulty = ArrayAdapter.createFromResource(this, R.array.difficulty_range, android.R.layout.simple_spinner_item);
        adapter_ingredient = ArrayAdapter.createFromResource(this, R.array.ingredient_range, android.R.layout.simple_spinner_item);
        adapter_flavor = ArrayAdapter.createFromResource(this, R.array.flavor_range, android.R.layout.simple_spinner_item);

        // Fix layout
        adapter_difficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_ingredient.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_flavor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapters to the spinners
        recipe_difficulty.setAdapter(adapter_difficulty);
        recipe_ingredient.setAdapter(adapter_ingredient);
        recipe_flavor.setAdapter(adapter_flavor);
    }

    public boolean isConnected(){

        // Check if the device is connected to wifi or mobile data
        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        return ni != null && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE) && ni.isConnected();
    }


    public void saveInformation(){

        // Check if it is connected to network
        if(!isConnected()){
            Toast.makeText(InsertActivity.this, "No network connection!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if there is image to upload
        else if(uri == null){
            Toast.makeText(InsertActivity.this, "Choose image to upload!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user typed a recipe name
        else if(recipe_name.getText().toString().isEmpty()){
            Toast.makeText(InsertActivity.this, "Input recipe name!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user typed a recipe budget
        else if(recipe_budget.getText().toString().isEmpty()){
            Toast.makeText(InsertActivity.this, "Input recipe budget!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user typed a recipe servings
        else if(recipe_servings.getText().toString().isEmpty()){
            Toast.makeText(InsertActivity.this, "Input recipe servings!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user typed recipe procedure
        else if(recipe_procedure.getText().toString().isEmpty()){
            Toast.makeText(InsertActivity.this, "Input recipe procedure!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user typed recipe creator
        else if(recipe_creator.getText().toString().isEmpty()){
            Toast.makeText(InsertActivity.this, "Input recipe creator!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress dialog
        prog_dial = ProgressDialog.show(InsertActivity.this, "Uploading", "Please wait...", true);

        // Create storage reference
        store_ref = FirebaseStorage.getInstance().getReference().child("Recipe").child(uri.getLastPathSegment());

        // Upload the image to storage and process the uploading of other information
        store_ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                uri_task = taskSnapshot.getStorage().getDownloadUrl();
                while(!uri_task.isComplete());
                url = uri_task.getResult();
                image_direc = url.toString();
                uploadInformation();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                prog_dial.dismiss();
            }
        });
    }

    public void uploadInformation(){

        // Get all typed and chosen information
        String name = recipe_name.getText().toString();
        String budget = recipe_budget.getText().toString();
        String difficulty = recipe_difficulty.getSelectedItem().toString();
        String ingredient = recipe_ingredient.getSelectedItem().toString();
        String flavor = recipe_flavor.getSelectedItem().toString();
        String servings = recipe_servings.getText().toString();
        String procedure = recipe_procedure.getText().toString();
        String creator = recipe_creator.getText().toString();

        // Create an object to store all information
        data = new DataClass(image_direc, name, budget, difficulty, ingredient, flavor, servings, procedure, creator);

        // Upload the object
        FirebaseDatabase.getInstance().getReference("Recipe").push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    // Dismiss dialog and show success toast
                    prog_dial.dismiss();
                    Toast.makeText(InsertActivity.this, "Recipe saved!", Toast.LENGTH_SHORT).show();

                    // Clear all previous selections and input
                    uri = null;
                    recipe_name.setText("");
                    recipe_procedure.setText("");
                    recipe_creator.setText("");
                    recipe_budget.setText("");
                    recipe_difficulty.setSelection(0);
                    recipe_ingredient.setSelection(0);
                    recipe_flavor.setSelection(0);
                    recipe_servings.setText("");
                    recipe_image.setImageResource(R.drawable.upload);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                prog_dial.dismiss();
            }
        });
    }
}