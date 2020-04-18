package com.HT.OneJunk;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "item_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView title = findViewById(R.id.title);
        title.setText(Item.getTitle());

        TextView description = findViewById(R.id.description);
        description.setText(Item.getDescription());

        TextView price = findViewById(R.id.price);
        price.setText(Item.getPrice());

        TextView seller = findViewById(R.id.seller);
        seller.setText(Item.getSeller());

        TextView created_on = findViewById(R.id.created_on);
        created_on.setText(Item.getCreatedTime());

    }
}





