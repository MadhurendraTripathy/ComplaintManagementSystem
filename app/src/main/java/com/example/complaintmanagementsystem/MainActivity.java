package com.example.complaintmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button cmsbutton;
    Button signup;
    Button login_btn;
    EditText emailid;
    EditText password;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        cmsbutton = findViewById(R.id.ComplaintsSectionBtn);
        signup = findViewById(R.id.signupactivitybtn);
        login_btn = findViewById(R.id.LoginButton);
        emailid = findViewById(R.id.editTextEnterEmailLogin);
        password = findViewById(R.id.editTextEnterPasswordLogin);


        if(mAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(MainActivity.this,HomePageAfterLogin.class));
        }

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    dialog = ProgressDialog.show(MainActivity.this, "", "Please wait while you are logged in...", true);
                    login();
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
            }
        });
    }



    boolean validate(){
        if(!emailid.getText().toString().trim().isEmpty() && !password.getText().toString().trim().isEmpty()){
            return true;
        }
        Toast.makeText(this, "Enter All Details", Toast.LENGTH_SHORT).show();
        return false;
    }

    void login(){
        mAuth.signInWithEmailAndPassword(emailid.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signin success", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "User Validated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,HomePageAfterLogin.class));
                            finish();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("signin failed", "signInWithEmail:failure", task.getException());
                            if(task.getException().toString().equals("com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred.")){
                                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }

                    }
                });
    }

}
