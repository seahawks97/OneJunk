package com.HT.OneJunk;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyPosts extends AppCompatActivity {

    private static final String TAG = "MyPosts";
    private static final String JUNK = "junk";
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ItemRecyclerAdapter mpAdapter;
    private FirebaseUser mpUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.myPost_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Query query = mDb.collection(JUNK)
                .whereEqualTo("seller", mpUser.getEmail())
                .orderBy("createdTime", Query.Direction.DESCENDING);

        //Gets info about the item to place into recycler view
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();

        mpAdapter = new ItemRecyclerAdapter(options, new ItemRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Item item){
                Intent aboutIntent = new Intent(MyPosts.this, DetailActivity.class);
                aboutIntent.putExtra("itemId", item.id);
                startActivity(aboutIntent);
            }
        });
        recyclerView.setAdapter(mpAdapter);
    }
    @Override
    public void onStart(){
        super.onStart();
        mpAdapter.startListening();
    }
    @Override
    public void onStop(){
        super.onStop();
        if (mpAdapter != null) {
            mpAdapter.stopListening();
        }
    }
}
