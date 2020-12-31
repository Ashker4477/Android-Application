package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UpdateProfile extends AppCompatActivity {
    private EditText newusername,newuseremail,newuserphone;
    private EditText newpassword;
    private ImageView Updatepic;
    String name,email,phone,password;
    private Button save;
    FirebaseAuth fba;
    FirebaseDatabase db;
    FirebaseStorage firebaseStorage;
    private int PICK_IMAGE=123;
    Uri imagepath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if((requestCode==PICK_IMAGE) && (resultCode==RESULT_OK)&&(data.getData()!=null) ){
            imagepath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                Updatepic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        newusername=findViewById(R.id.usrnm);
        newuseremail=findViewById(R.id.usrem);
        newuserphone=findViewById(R.id.usrph);
        Updatepic=findViewById(R.id.userimage);
        save=findViewById(R.id.usrupd);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fba=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();

        final DatabaseReference ref=db.getReference(fba.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                newusername.setText(userProfile.getUserName());
                newuseremail.setText(userProfile.getUserEmail());
                newuserphone.setText(userProfile.getPhone());
              //  newpassword.setText(userProfile.getUserPassword());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfile.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        final StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(fba.getUid()).child("Images").child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(Updatepic);

            }
        });
        Updatepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");  //application/pdf/doc/* //audio/mp3/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),PICK_IMAGE);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 name=newusername.getText().toString();
                 email=newuseremail.getText().toString();
                 phone=newuserphone.getText().toString();
                UserProfile userProfile=new UserProfile(email,name,password,phone);
                ref.setValue(userProfile);
                StorageReference imageReference=storageReference.child(fba.getUid()).child("Images").child("Profile Pic"); //userid/images/profile pic
                UploadTask uploadTask=imageReference.putFile(imagepath);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UpdateProfile.this, "Upload Success..", Toast.LENGTH_SHORT).show();
                    }
                });
                //sendEmailVerification();
                updateemail();
                finish();

            }
        });

    }
    private void updateemail() {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference ref=db.getReference(fba.getUid());
        FirebaseUser user =fba.getCurrentUser();
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateProfile.this, "email updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UpdateProfile.this, "email updated failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    private void sendEmailVerification() {
        FirebaseUser user = fba.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                       updateemail();
                        Toast.makeText(UpdateProfile.this, "email Verification mail sent!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateProfile.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
