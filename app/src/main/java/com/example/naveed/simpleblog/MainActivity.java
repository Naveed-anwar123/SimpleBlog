package com.example.naveed.simpleblog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;


import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference dbref;


    //Login with firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Must add dependencies for card view nad recycle view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                   // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                   // Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);      //Recycle view used in activit_main.xml
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);     //To show latest post first
        //mLayoutManager.setStackFromEnd(true);﻿
        mRecyclerView.setLayoutManager(mLayoutManager);



        dbref = FirebaseDatabase.getInstance().getReference().child("Blog");   // DB node reference

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add,menu);                  //to show plus sign in actionbar , see menu folder inside res
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.addnew)
        {
            Intent i = new Intent(MainActivity.this , PostActivity.class);
            startActivity(i);                                                  // Opening post activity
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        // Important , follow documentation
        FirebaseRecyclerAdapter<Blog , BlogHelper> firebaseRecyclerAcapter = new FirebaseRecyclerAdapter<Blog, BlogHelper>(
                Blog.class,
                R.layout.single_post,  // view for sigle post uses card view
                BlogHelper.class,
                dbref
        ) {
            @Override
            protected void populateViewHolder(BlogHelper viewHolder, Blog model, int position) {
                    viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }

        };
        mRecyclerView.setAdapter(firebaseRecyclerAcapter);

    }
    //Helper class
    public static class BlogHelper extends RecyclerView.ViewHolder {   //Inner class for reading purposes
        View view;   // View reference
        public BlogHelper(View itemView) {
            super(itemView);
            view = itemView;   // initialize after super method
        }
        public void setTitle(String title){
            TextView tview = (TextView) view.findViewById(R.id.stitle);
            tview.setText(title);

        }
        public void setDescription(String desc){
            TextView tdesc = (TextView) view.findViewById(R.id.sdecs);
            tdesc.setText(desc);
        }
        public void setImage(Context content , String image){
            ImageView img = (ImageView) view.findViewById(R.id.simage);
            Picasso.with(content).load(image).into(img);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
