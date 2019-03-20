package com.example.mobileapplicationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Pattern;

public class CreateUser extends AppCompatActivity {
    private EditText txtFirstName;
    private EditText txtLastName;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private EditText txtBio;
    private EditText txtEmail;
    private EditText txtPassword1;
    private EditText txtPassword2;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    private static final String emailRegex = "[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z]{2,3}[.] {0,1}[a-zA-Z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);


        txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
        radioFemale = (RadioButton)findViewById(R.id.radioFemale);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        txtBio = (EditText)findViewById(R.id.txtBio);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword1 = (EditText)findViewById(R.id.txtPassword1);
        txtPassword2 = (EditText)findViewById(R.id.txtPassword2);

        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        txtFirstName.setAnimation(fadeIn);
        txtLastName.setAnimation(fadeIn);
        radioMale.setAnimation(fadeIn);
        radioFemale.setAnimation(fadeIn);
        txtPassword2.setAnimation(fadeIn);
        txtBio.setAnimation(fadeIn);
        txtEmail.setAnimation(fadeIn);
        txtPassword1.setAnimation(fadeIn);
        txtPassword2.setAnimation(fadeIn);

        //Create an Instance from Fire base Database
        database = FirebaseDatabase.getInstance();
        //Create new Category
        dbRef = database.getReference("MUsers");

        //Create new Instance from Fire base Authorisation
        mAuth = FirebaseAuth.getInstance();
    }

    //Create a new Account
    public void createAccount(View v){


        final String fName = txtFirstName.getText().toString().trim();
        final String lName = txtLastName.getText().toString().trim();
        final String bio = txtBio.getText().toString().trim();
        final String gender = (radioMale.isChecked()) ? "Male" : "Female";

        String email, pwd;
        if(Pattern.matches(emailRegex,txtEmail.getText().toString().trim()))
        {
            email = txtEmail.getText().toString();
        }
        else
        {
            Toast.makeText(this,"Invalid Email Address.",Toast.LENGTH_LONG).show();
            return;
        }

        if(Objects.equals(txtPassword1.getText().toString(),txtPassword2.getText().toString()))
        {
            pwd = txtPassword1.getText().toString();
        }
         else
        {
            Toast.makeText(this,"Passwords should be same.",Toast.LENGTH_LONG).show();
            return;
        }


        //Create new user using email and password to get access to database
        try
        {
            mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                //Get user ID to use as Reference
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserRef = dbRef.child(userId);
                                    currentUserRef.child("firstName").setValue(fName);
                                    currentUserRef.child("lastName").setValue(lName);
                                    currentUserRef.child("gender").setValue(gender);
                                    currentUserRef.child("bio").setValue(bio);
                                Toast.makeText(CreateUser.this,"User has been Created.",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CreateUser.this, PostListActivity.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(CreateUser.this,"Authentication failed.",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Toast.makeText(CreateUser.this,e.toString(),Toast.LENGTH_LONG).show();
        }



    }
}
