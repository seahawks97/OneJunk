package com.HT.OneJunk;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private static final String JUNK = "junk";
    private static final String CURRENT_IMAGE = "currentImage";

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private FirebaseUser dUser = FirebaseAuth.getInstance().getCurrentUser();

    private List<Item> imageList = new ArrayList<>();
    private int mCurrentImage = 0;

    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mImageView = findViewById(R.id.image);

        mDb.collection(JUNK).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    imageList = new ArrayList<>();
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Item image = document.toObject(Item.class);
                        imageList.add(image);
                    }
                    updateUI(mCurrentImage);
                } else {
                    Log.w(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

//        mStorageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                //Handle errors if this works
//            }
//        });



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

                    TextView seller = findViewById(R.id.seller_in);
                    seller.setText(item.getSeller());

                    // if the post email id matches the current user id, enable buttons
                    if (isMyPost(item.getSeller())) {
                        // enable edit button
                        Button editBtn = findViewById(R.id.edit_button);
                        editBtn.setClickable(true);
                        editBtn.setVisibility(View.VISIBLE);

                        // enable delete button
                        Button delBtn = findViewById(R.id.delete_button);
                        delBtn.setClickable(true);
                        delBtn.setVisibility(View.VISIBLE);
                    }

                } else {
                    Log.d(TAG, "get failed with", task.getException());
                }
            }

        });
    }

    private void updateUI(int imageNum){
        if(imageList.isEmpty()||imageNum<0||imageNum>imageList.size() ){
            mImageView.setVisibility(View.GONE);
        }else{
            mImageView.setVisibility(View.VISIBLE);
            StorageReference image = mStorageRef.child(imageList.get(imageNum).getImage());
            GlideApp.with(DetailActivity.this).load(image).into(mImageView);

        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        updateUI(mCurrentImage);
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPeristentState){
        outState.putInt(CURRENT_IMAGE, mCurrentImage);
        super.onSaveInstanceState(outState, outPeristentState);
        GlideApp.with(DetailActivity.this).pauseAllRequests();
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        GlideApp.with(DetailActivity.this).resumeRequests();

        if(savedInstanceState != null){
            mCurrentImage = savedInstanceState.getInt(CURRENT_IMAGE);

        }
    }

    public void contact(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, R.id.seller_in);
        intent.putExtra(Intent.EXTRA_SUBJECT, R.id.title_in);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    public void editPost(View view) {
        // code needed here for when a user wants to edit their post
        // Go to NewPostActivity, bring all the data with as an intent?
        // or just go to EditPostActivity?

        // Attempt 1: Using intents
        /*Intent intent = new Intent(DetailActivity.this, NewPostActivity.class);
        TextView title = findViewById(R.id.title_in);
        TextView desc = findViewById(R.id.description_in);
        TextView price = findViewById(R.id.price_in);
        intent.putExtra("title", title.getText());
        intent.putExtra("description", desc.getText());
        intent.putExtra("price", price.getText());
        // need to get the photo(s) as well

        startActivity(intent);
*/
    }

    public void deletePost(View view) {
        // https://stackoverflow.com/a/13511580/10072355
        // Create an alert popup to confirm user wants to delete the post
        AlertDialog.Builder b1 = new AlertDialog.Builder(DetailActivity.this);
        b1.setMessage("Are you sure you want to delete this post?");
        b1.setCancelable(true);
        b1.setPositiveButton(
                "Yes, delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Get item
                        Intent fromWA = getIntent();
                        String itemId = fromWA.getStringExtra("itemId");

                        // https://firebase.google.com/docs/firestore/manage-data/delete-data
                        // delete the item from the DB
                        mDb.collection(JUNK).document(itemId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        Intent intent = new Intent(DetailActivity.this, WelcomeActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(DetailActivity.this, "Post deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                        Toast.makeText(DetailActivity.this, "Unable to delete post", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                }
        );
        b1.setNegativeButton(
                "No, keep",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );

        AlertDialog alert1 = b1.create();
        alert1.show();
    }

    private boolean isMyPost(String email) {
        String curEmail = dUser.getEmail();
        return (curEmail.equals(email));
    }

}
