package com.HT.OneJunk;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewPostActivity extends AppCompatActivity {

    private TextView npTitle;
    private EditText npTitleIn;
    private TextView npDescription;
    private EditText npDescriptionIn;
    private TextView npPrice;
    private EditText npPriceIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        npTitle = findViewById(R.id.title_label);
        npTitleIn = findViewById(R.id.title_in);
        npDescription = findViewById(R.id.description_label);
        npDescriptionIn = findViewById(R.id.description_in);
        npPrice = findViewById(R.id.price_label);
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
        } else if ((price.charAt(price.length() - 2) != '.') || (price.split(".")[1].length() > 2)){
            // if price doesn't include a '.' to denote dollars/cents
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

}
