package com.example.loginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.zip.Inflater;

public class next extends AppCompatActivity {

    private Button Lg;
    private FirebaseAuth fba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        fba=FirebaseAuth.getInstance();
        Lg= findViewById(R.id.lgout);

        Lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              logout();
            }
        });

    }

    private void logout(){
        fba.signOut();
        startActivity(new Intent(next.this,MainActivity.class));
         finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.lgoutMenu:{
                logout();
                break;
            }
            case R.id.profile:{
                startActivity(new Intent(next.this,ProfileActivity.class));
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
