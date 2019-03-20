package com.example.mobileapplicationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private EditText txtTitle;
    private EditText txtDescription;
    private Button btnPost;
    private ImageView imgPost;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mPostDatabase;
    private FirebaseUser mUser;
    private StorageReference mStorage;

    private static final int SELECT_IMAGE = 1;

    private Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        txtTitle = (EditText)findViewById(R.id.txtPostTitle);
        txtDescription = (EditText) findViewById(R.id.txtPostDescription);
        btnPost = (Button)findViewById(R.id.btnPost);
        imgPost = (ImageView)findViewById(R.id.imgPost);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);


        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("MBlog");

        //to access storage on firebase
        mStorage = FirebaseStorage.getInstance().getReference();

        //Open Gallery to select a photo
        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //to select all type of the photos
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == SELECT_IMAGE)
            {
                if(resultCode == RESULT_OK)
                {
                    mImageUri = data.getData();
                    imgPost.setImageURI(mImageUri);
                }
            }
    }

    public void submitPost(View v)
    {
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();

            if(!title.equals("") && !description.equals("") && mImageUri != null)
            {
                //Start to Upload

                StorageReference filepath = mStorage.child("MBlog_imaged").child(mImageUri.getLastPathSegment());
                filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //To get the path of the Image from Storage
                        //Uri imgaeUri = taskSnapshot.getUploadSessionUri();
                        //to create new item with a unique reference
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri imgaeUri = urlTask.getResult();


                        DatabaseReference newPost = mPostDatabase.push();
                        Map<String, String> dataToSave = new HashMap<>();
                        dataToSave.put("title", txtTitle.getText().toString());
                        dataToSave.put("desc",txtDescription.getText().toString());
                        dataToSave.put("image", imgaeUri.toString());
                        dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                        dataToSave.put("userid",mUser.getUid());

                        newPost.setValue(dataToSave);

                        startActivity(new Intent(AddPostActivity.this, PostListActivity.class));
                        finish();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(1);
                    }
                });


            }
    }
}
