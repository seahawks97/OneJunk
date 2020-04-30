package com.HT.OneJunk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private static final String JUNK = "junk";
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");
        Log.d(TAG, "Requesting item details from Firestore for item with id" + itemId);

        DocumentReference docRef = mDb.collection(JUNK).document(itemId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "Document Snapshot data:" + document.getData());
                    Item item = document.toObject(Item.class);
                    Log.d(TAG, "Converted document to Item class. Item ID: " + item.id);

                    TextView title_in = findViewById(R.id.title_in);
                    title_in.setText(item.getTitle());

                    TextView description_in = findViewById(R.id.description_in);
                    description_in.setText(item.getDescription());

                    TextView price_in = findViewById((R.id.price_in));
                    price_in.setText(item.getPrice());

                    TextView seller = findViewById(R.id.seller);
                    seller.setText(item.getSeller());


                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }

        });
    }

    public void contact(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, R.id.seller);
        intent.putExtra(Intent.EXTRA_SUBJECT, R.id.title_in);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

}
