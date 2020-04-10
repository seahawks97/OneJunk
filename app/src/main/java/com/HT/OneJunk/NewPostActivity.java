package com.HT.OneJunk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

public class NewPostActivity extends AppCompatActivity {
    private TextView npHeader;
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

        npHeader = findViewById(R.id.create_post_title);
        npTitle = findViewById(R.id.title_label);
        npTitleIn = findViewById(R.id.title_in);
        npDescription = findViewById(R.id.description_label);
        npDescriptionIn = findViewById(R.id.description_in);
        npPrice = findViewById(R.id.price_label);
        npPriceIn = findViewById(R.id.price_in);
    }

    private boolean validateForm() {
        boolean valid = true;

        String title = npTitleIn.getText().toString();
        if (TextUtils.isEmpty(title)) {
            npTitleIn.setError("Required.");
            valid = false;
        } else {
            npTitleIn.setError(null);
        }

        String desc = npDescriptionIn.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            npDescriptionIn.setError("Required.");
            valid = false;
        } else {
            npDescriptionIn.setError(null);
        }

        String price = npPriceIn.getText().toString();
        if (TextUtils.isEmpty(price)) {
            npPriceIn.setError("Required.");
            valid = false;
        } else {
            npPriceIn.setError(null);
        }

        return valid;
    }

}
