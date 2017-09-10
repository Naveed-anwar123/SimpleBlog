package com.example.naveed.simpleblog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by Naveed Anwar on 10/09/2017.
 */

public class SinglePostActivity extends AppCompatActivity {
    private String key;

    private ImageView img;
    private Button remove;
    private TextView title , desc;

    FirebaseAuth mAuth;
    DatabaseReference db,db1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_post_activity);
        img = (ImageView)findViewById(R.id.imageView);
        title = (TextView)findViewById(R.id.title);
        desc = (TextView)findViewById(R.id.desc);
        remove = (Button)findViewById(R.id.remove);
        key = getIntent().getExtras().getString("key");
        db = FirebaseDatabase.getInstance().getReference().child("Blog").child(key);
        db1 = FirebaseDatabase.getInstance().getReference().child("Blog");
        mAuth = FirebaseAuth.getInstance();


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    title.setText(dataSnapshot.child("title").getValue().toString());
                    desc.setText(dataSnapshot.child("desc").getValue().toString());
                    String url = (String) dataSnapshot.child("image").getValue();
                    String id = (String) dataSnapshot.child("uid").getValue();
                    Picasso.with(SinglePostActivity.this).load(url).into(img);
                    if (mAuth.getCurrentUser().getUid().equals(id)) {
                        remove.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db1.child(key).removeValue();
                startActivity(new Intent(SinglePostActivity.this,MainActivity.class));
                finish();
            }
        });




    }
}
