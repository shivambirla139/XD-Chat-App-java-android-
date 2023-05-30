package com.newapps.xdchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.newapps.xdchat.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignInActivity.this);

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = binding.txtEmail.getText().toString().trim();
                String password = binding.txtPassword.getText().toString().trim();

                if(!binding.txtEmail.getText().toString().isEmpty()
                && !binding.txtPassword.getText().toString().isEmpty()){
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "Enter Credentials", Toast.LENGTH_SHORT).show();

                }
            }
        });

        binding.txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                finish();
            }
        });

        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }



    }
}