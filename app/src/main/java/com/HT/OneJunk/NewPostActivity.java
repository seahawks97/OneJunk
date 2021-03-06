package com.HT.OneJunk;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class NewPostActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";
    private static final String JUNK = "junk";
    private FirebaseFirestore npDb = FirebaseFirestore.getInstance();
    private FirebaseUser npUser = FirebaseAuth.getInstance().getCurrentUser();

    private StorageReference npStorageRefPost = FirebaseStorage.getInstance().getReference();

    private EditText npTitleIn;
    private EditText npDescriptionIn;
    private EditText npPriceIn;
    private Button npImageUpload;
    private ImageView npImage;
    public Uri imageUri;



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
        npImageUpload = findViewById(R.id.imageUpload);
        npImage = findViewById(R.id.image);

        // if coming from an intent, populate the fields
        // TODO: pass picture in too
        Intent fromDA = getIntent();
        if (fromDA != null) {
            // get data from intent
            String newTitle = fromDA.getStringExtra("title");
            String newDesc = fromDA.getStringExtra("description");
            String newPrice = fromDA.getStringExtra("price");

            // set the fields
            npTitleIn.setText(newTitle);
            npDescriptionIn.setText(newDesc);
            npPriceIn.setText(newPrice);
        }

        npImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

    }

    private void FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        String[] imageTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, imageTypes);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            npImage.setImageURI(imageUri); // places the image into the Image View in NPA

        }
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

        if (npImage.getHeight() == 0) {
            npImageUpload.setError("Required.");
            valid = false;
        } else {
            npImageUpload.setError(null);
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
        } else if (price.charAt(0) == '-') { // price is negative
            npPriceIn.setError("Price must be positive.");
            valid = false;
        } else { // price is valid
            npPriceIn.setError(null);
        }

        return valid;
    }

    private static final String STORAGE_IMAGE_ROOT = "images";

    public void submitPost(View view) {
        if (!validateForm()) {
            return;
        }

        // get string info
        String title = npTitleIn.getText().toString();
        String desc = npDescriptionIn.getText().toString();
        String price = npPriceIn.getText().toString();
        String imageFileName = imageUri.getLastPathSegment() + '.' + getExtension(imageUri);
        fileUploader(imageUri, STORAGE_IMAGE_ROOT, imageFileName);



        // get userID
        String userID = npUser.getEmail();

        // prepare data: create an Item of data
        Item post = new Item(title, desc, price, userID, new Date(), STORAGE_IMAGE_ROOT + '/' + imageFileName);

        Toast.makeText(this, "Adding " + title + "...", Toast.LENGTH_SHORT).show();

        // somewhere around here, if intent exists, just update the post

        // add to collection
        npDb.collection(JUNK).add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "addPostToFirestore:success" + documentReference.getId());
                            Intent intent = new Intent(NewPostActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            Toast.makeText(NewPostActivity.this, "Post added!", Toast.LENGTH_SHORT).show();
                        }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "addPostToFirestore:failure", e);
                    }
                });

    }

    public void cancelPost (View view) {
        AlertDialog.Builder b1 = new AlertDialog.Builder(NewPostActivity.this);
        b1.setMessage("Are you sure you want to cancel making this post?");
        b1.setCancelable(true);

        b1.setPositiveButton(
                "Yes, cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Just go back to WelcomeActivity, no saving to Firestore
                        Intent intent = new Intent(NewPostActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                    }
                }
        );
        b1.setNegativeButton(
                "No, keep editing",
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

    private String getExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void fileUploader(Uri imageUri, String imageRoot, String imageFileName){

        StorageReference Ref = npStorageRefPost.child(imageRoot).child(imageFileName);
        Ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(NewPostActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }


}
