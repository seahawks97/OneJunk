package com.HT.OneJunk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private static final String JUNK = "junk";
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ItemRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.menu_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Query query = mDb.collection(JUNK).whereEqualTo("title_in", "chair");
        Intent intent = getIntent();
        String itemId = intent.getStringExtra("itemId");
        Log.d(TAG, "Requesting item details from Firestore for item with id" + itemId);

        DocumentReference docRef = mDb.collection(JUNK).document(itemId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "Document Snapshot data:" + document.getData());
                    Item item = document.toObject(Item.class);
                    Log.d(TAG, "Converted document to Item class. Item ID: " + item.id);
                }else{
                    Log.d(TAG, "get failed with", task.getException());
                }
            }
        });
//        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();
//        mAdapter = new ItemRecyclerAdapter(options);
//        recyclerView.setAdapter(mAdapter);




//        mDb.collection(JUNK).whereEqualTo("title", "chair").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(QueryDocumentSnapshot document:task.getResult()){
//                        Log.d(TAG, document.getId() + "=>" + document.getData());
//                    }
//                }else{
//                    Log.d(TAG, "Error getting posts:", task.getException());
//                }
//            }
//        });

    }
}





