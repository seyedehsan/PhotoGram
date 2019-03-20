package com.example.mobileapplicationproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView txtRegister;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public  FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Animation fadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtEmail.startAnimation(fadeIn);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPassword.startAnimation(fadeIn);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.startAnimation(fadeIn);
        txtRegister = (TextView)findViewById(R.id.txtRegister);
        txtRegister.setAnimation(fadeIn);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Toast.makeText(LoginActivity.this,"You are Signed in",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, PostListActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"You are not Signed in",Toast.LENGTH_LONG).show();
                }
            }
        };

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,CreateUser.class);
                startActivity(intent);
            }
        });
    }

    //add functionality to menu to sign out
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_signout)
        {
            mAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    //to display menu on activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Check if the user is Signed In before or not
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    //Remove the user Authentication
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    public void userSignIn(View v)
    {
        String email = txtEmail.getText().toString();
        String pwd = txtPassword.getText().toString();

        //check email and password
        if(email.equals("") || pwd.equals(""))
        {

            Toast.makeText(this,"please enter something",Toast.LENGTH_LONG).show();
        }
        else
        {
            try
            {
                //Login to Firebase using email and password
                mAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    //Get default user
                                    user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this,"Signed In",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, AddPostActivity.class));
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,task.toString()+" ",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
            catch (Exception e)
            {
                Toast.makeText(LoginActivity.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}

