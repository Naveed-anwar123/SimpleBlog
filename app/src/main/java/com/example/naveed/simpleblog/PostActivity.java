package com.example.naveed.simpleblog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton img;
    private static int GALLERY_REQUEST=1;
    private EditText title;
    private EditText desc;
    private Button post;
    private Uri image;
    private StorageReference myref;
    private DatabaseReference dbref;
    private ProgressDialog pg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        img = (ImageButton)findViewById(R.id.img);
        desc =(EditText)findViewById(R.id.desc);
        title =(EditText)findViewById(R.id.title);
        post = (Button)findViewById(R.id.post);
        myref = FirebaseStorage.getInstance().getReference();
        dbref = FirebaseDatabase.getInstance().getReference().child("Blog");
        pg = new ProgressDialog(this);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });





        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gellery = new Intent(Intent.ACTION_GET_CONTENT);
                gellery.setType("image/*");
                startActivityForResult(gellery,GALLERY_REQUEST);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK)
        {
             image = data.getData();
            img.setImageURI(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void startPosting(){
        pg.setMessage("Posting, Please wait!");
        pg.show();
        final String ptitle = title.getText().toString().trim();
        final String pdesc = title.getText().toString().trim();    // need to declare final because i am accessing in an inner class below
        if(!ptitle.equals("") && !pdesc.equals("") && image !=null)
        {
            StorageReference ref = myref.child("Blog_images").child(image.getLastPathSegment());
            ref.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri i = taskSnapshot.getDownloadUrl();   //get download uri to save into realtime database
                    DatabaseReference db = dbref.push();
                    db.child("title").setValue(ptitle);
                    db.child("desc").setValue(pdesc);
                    db.child("image").setValue(i.toString());
                    pg.dismiss();
                    startActivity(new Intent(PostActivity.this,MainActivity.class));
                }
            });
        }
    }

}