package com.example.naveed.simpleblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Naveed Anwar on 09/09/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private String uemail,upassword;
    private EditText email,password;
    private TextView link;
    private Button login;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog pg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.login);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        link = (TextView)findViewById(R.id.link_signup);
        mAuth = FirebaseAuth.getInstance();
        pg = new ProgressDialog(this);
        pg.setMessage("Authenticating....");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                } else {
                    // User is signed out
                 //   Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                uemail = email.getText().toString();
                upassword = password.getText().toString();
                if(!uemail.equals("") && !upassword.equals(""))
                {

                    startLogin();

                }
            }
        });

    link.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

        }
    });


    }

    public void startLogin(){
        pg.show();
        mAuth.signInWithEmailAndPassword(uemail, upassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            pg.dismiss();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }
                        else
                        {
                            pg.dismiss();
                            Toast.makeText(LoginActivity.this,"Email or password is incorrect",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }


}
