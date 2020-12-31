package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName,profileEmail,profilePhone;
    private ImageView profilepic;
    private Button edit;
    private FirebaseAuth fba;
    private FirebaseDatabase db;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilepic=findViewById(R.id.propic);
        profileName=findViewById(R.id.profname);
        profileEmail=findViewById(R.id.profemail);
        profilePhone=findViewById(R.id.profphone);
        edit=findViewById(R.id.profed);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fba=FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();


        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(fba.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(profilepic);

            }
        });
        DatabaseReference ref=db.getReference(fba.getUid());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                profileName.setText("Name:"+userProfile.getUserName());
                profileEmail.setText("Email:"+userProfile.getUserEmail());
                profilePhone.setText("Phone:"+userProfile.getPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,UpdateProfile.class));

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.refresh:{
                recreate();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
