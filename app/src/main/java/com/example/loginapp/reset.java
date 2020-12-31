package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class reset extends AppCompatActivity {
    private EditText rse;
    private Button rs;
    private FirebaseAuth fba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        rse= findViewById(R.id.rsemail);
        rs= findViewById(R.id.rsb);
        fba=FirebaseAuth.getInstance();

        rs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rsemail=rse.getText().toString().trim();
                if(rsemail.isEmpty()) {
                    Toast.makeText(reset.this,"pls enter a valid email",Toast.LENGTH_SHORT).show();

                }else{
                    fba.sendPasswordResetEmail(rsemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()) {
                           Toast.makeText(reset.this,"password reset email send",Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(reset.this,MainActivity.class));
                           finish();
                       }else {
                           Toast.makeText(reset.this,"couldn't find registered email",Toast.LENGTH_SHORT).show();

                       }
                        }
                    });
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

}
