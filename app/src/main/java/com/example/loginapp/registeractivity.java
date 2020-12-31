package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.UserHandle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.firebase_auth.zzff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.MultiFactor;
import com.google.firebase.auth.MultiFactorInfo;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;

public class registeractivity extends AppCompatActivity {
    private ImageView profilepic;
    private EditText userName, userEmail, userPassword;
    private EditText phone;
    private Button Re;
    private TextView al;
    String name, email, ph, password,propic;
    private FirebaseAuth fba;
    private FirebaseStorage fs;
    private StorageReference storageReference;
    private int PICK_IMAGE=123;
    Uri imagepath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if((requestCode==PICK_IMAGE) && (resultCode==RESULT_OK)&&(data.getData()!=null) ){
           imagepath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
                profilepic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeractivity);

        setupUIViews();
        fba = FirebaseAuth.getInstance();
        fs=FirebaseStorage.getInstance();
        storageReference=fs.getReference();

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");  //application/pdf/doc/* //audio/mp3/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),PICK_IMAGE);

            }
        });

        Re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    email = userEmail.getText().toString();
                    password = userPassword.getText().toString();
                    fba.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // sendEmailVerification();
                                sendUserdata();
                                Toast.makeText(registeractivity.this, "Registration successful, user data is uploaded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(registeractivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(registeractivity.this, "Registration failed...already a user", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });

        al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registeractivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setupUIViews() {
        profilepic=findViewById(R.id.userpro);
        userName = findViewById(R.id.un);
        userEmail = findViewById(R.id.ue);
        userPassword = findViewById(R.id.up);
        phone = findViewById(R.id.uf);
        Re = findViewById(R.id.reg);
        al = findViewById(R.id.Rd);
    }

    private Boolean validate() {
        Boolean result = false;
        email = userEmail.getText().toString().trim();
        name = userName.getText().toString().trim();
        ph = phone.getText().toString().trim();
        password = userPassword.getText().toString().trim();
        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || ph.isEmpty()|| imagepath==null) {
            Toast.makeText(registeractivity.this, "pls enter all details", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

    private void sendEmailVerification() {
        FirebaseUser user = fba.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserdata();
                        Toast.makeText(registeractivity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        fba.signOut();
                        startActivity(new Intent(registeractivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(registeractivity.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserdata() {

        FirebaseDatabase Db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = Db.getReference(fba.getUid());
        StorageReference imageReference=storageReference.child(fba.getUid()).child("Images").child("Profile Pic"); //userid/images/profile pic
        UploadTask uploadTask=imageReference.putFile(imagepath);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(registeractivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(registeractivity.this, "Upload Success..", Toast.LENGTH_SHORT).show();
            }
        });

        UserProfile userProfile = new UserProfile(email, name, password, ph);
        myRef.setValue(userProfile);
    }

}