package com.example.complaintmanagementsystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomePageAfterLogin extends AppCompatActivity {

    Button cmsbutton;
    Button LogoutBtn;
    TextView name,unitid;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String fetchunitid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_after_login);


        cmsbutton = findViewById(R.id.ComplaintsSectionBtn);
        LogoutBtn = findViewById(R.id.LogoutButton);
        name = findViewById(R.id.showNameInHomepage);
        unitid = findViewById(R.id.showunitidinhomepage);
        mAuth = FirebaseAuth.getInstance();


        SetUpUser user;
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Toast.makeText(HomePageAfterLogin.this, documentSnapshot.get("unitID").toString(), Toast.LENGTH_SHORT).show();
                fetchunitid = documentSnapshot.get("unitID").toString();
                unitid.setText(fetchunitid);
                name.setText(documentSnapshot.get("fname").toString()+" "+documentSnapshot.get("lname").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePageAfterLogin.this, "Failed To Load Information", Toast.LENGTH_SHORT).show();
            }
        });


        cmsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(HomePageAfterLogin.this, "fetchunitid="+fetchunitid, Toast.LENGTH_LONG).show();
                startCmsActivity();
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                startActivity(new Intent(HomePageAfterLogin.this,MainActivity.class));
            }
        });

    }


    void startCmsActivity(){
        Intent intent = new Intent(HomePageAfterLogin.this,CMSactivity.class);
        //Log.d("passing fetched intent",fetchunitid);

        if(fetchunitid!=null){
            intent.putExtra("fetchedunitid",fetchunitid);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Please Wait While Your Details Are Loaded", Toast.LENGTH_SHORT).show();
        }
    }

}
