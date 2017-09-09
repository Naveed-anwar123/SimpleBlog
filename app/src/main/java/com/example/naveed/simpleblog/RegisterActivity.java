package com.example.naveed.simpleblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Naveed Anwar on 09/09/2017.
 */

public class RegisterActivity  extends AppCompatActivity {


    private String uemail,upassword;
    private EditText email,password;

    private Button register;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog pg;
    private TextView link;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = (Button)findViewById(R.id.register);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        pg = new ProgressDialog(this);
        pg.setMessage("Creating you account...");
        mAuth = FirebaseAuth.getInstance();
        link = (TextView)findViewById(R.id.link_login);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uemail = email.getText().toString();
                upassword = password.getText().toString();
                if(!uemail.equals("") && !upassword.equals(""))
                {

                    startRegister();

                }
            }
        });

    }

    public void startRegister(){
        pg.show();
        mAuth.createUserWithEmailAndPassword(uemail, upassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            pg.dismiss();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            pg.dismiss();
                            Toast.makeText(RegisterActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}
