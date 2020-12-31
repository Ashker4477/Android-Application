package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView info;
    private TextView Reg;
    private TextView fg;
    private int counter=5;
    private FirebaseAuth fba;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email= findViewById(R.id.user);
       password= findViewById(R.id.pass);
       login= findViewById(R.id.button);
       info= findViewById(R.id.info);
       Reg= findViewById(R.id.registration);
       fg= findViewById(R.id.fgp);
       info.setText("no of attempt remaining: 5");

        pd=new ProgressDialog(this);
        fba=FirebaseAuth.getInstance();
        FirebaseUser user=fba.getCurrentUser();
        if(user !=null){
            startActivity(new Intent(MainActivity.this,next.class));
            finish();
        }

       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               validate(email.getText().toString(),password.getText().toString());

           }
       });
       Reg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,registeractivity.class));

           }
       });

    }
       private void validate(String userName, String userPassword) {
           if (((userName.isEmpty()) && (userPassword.isEmpty()))||((userName.isEmpty())||(userPassword.isEmpty()))) {
               Toast.makeText(MainActivity.this, "pls enter username and password", Toast.LENGTH_SHORT).show();
           } else if (((userName != null) && (userPassword != null))&&((userName!=null)||(userPassword!=null))) {
               pd.setMessage("pls wait to progress");
               pd.show();
               fba.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           pd.dismiss();
                           Toast.makeText(MainActivity.this, "login success", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(MainActivity.this, next.class));
                           finish();
                       } else {
                           pd.dismiss();
                           counter--;
                           Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                           info.setText("no of attempt remaing: " + (counter));
                           if (counter == 0) {
                               login.setEnabled(false);
                               fg.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       startActivity(new Intent(MainActivity.this,reset.class));
                                       finish();
                                   }
                               });
                           }
                       }
                   }
               });
           }
       }
    }
