package com.HT.OneJunk;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class NewPostActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";
    private FirebaseFirestore npDb = FirebaseFirestore.getInstance();
    private FirebaseUser npUser = FirebaseAuth.getInstance().getCurrentUser();

    private EditText npTitleIn;
    private EditText npDescriptionIn;
    private EditText npPriceIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        npTitleIn = findViewById(R.id.title_in);
        npDescriptionIn = findViewById(R.id.description_in);
        npPriceIn = findViewById(R.id.price_in);
    }

    private boolean validateForm() {
        // Called when the user hits submit post
        boolean valid = true;

        String title = npTitleIn.getText().toString();
        if (TextUtils.isEmpty(title)) {
            npTitleIn.setError("Required.");
            valid = false;
        } else { // title is filled in
            npTitleIn.setError(null);
        }

        String desc = npDescriptionIn.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            npDescriptionIn.setError("Required.");
            valid = false;
        } else { // description is filled in
            npDescriptionIn.setError(null);
        }

        String price = npPriceIn.getText().toString();
        if (TextUtils.isEmpty(price)) {
            npPriceIn.setError("Required.");
            valid = false;
        } else if ((price.length() < 4) || (price.charAt(price.length() - 3) != '.') || (price.split(".")[1].length() > 2)){
            // if price doesn't include a '.' to properly denote dollars/cents
            npPriceIn.setError("Price must be in dollars and cents.");
            valid = false;
        } else if (price.charAt(0) == '-') { // price is negative
            npPriceIn.setError("Price must be positive.");
            valid = false;
        } else { // price is valid
            npPriceIn.setError(null);
        }

        return valid;
    }

    public void submitPost(View view) {
        if (!validateForm()) {
            return;
        }

        // get string info
        String title = npTitleIn.getText().toString();
        String desc = npDescriptionIn.getText().toString();
        String price = npPriceIn.getText().toString();

        // get userID
        String userID = npUser.getUid();

        // prepare data: create HashMap of data
        Item post = new Item(title, desc, price, userID, new Date());

        // add to collection
        npDb.collection("users").add(post)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "addPostToFirestore:success");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "addPostToFirestore:failure");
            }
        });


    }

}
