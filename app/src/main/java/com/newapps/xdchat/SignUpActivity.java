package com.newapps.xdchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.newapps.xdchat.Models.User;
import com.newapps.xdchat.databinding.ActivitySignInBinding;
import com.newapps.xdchat.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are Creating your account");

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtUsername.getText().toString().isEmpty()
                && !binding.txtEmail.getText().toString().isEmpty()
                && !binding.txtPassword.getText().toString().isEmpty()){
                    progressDialog.show();
                    String email = binding.txtEmail.getText().toString().trim();
                    String password = binding.txtPassword.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                User user = new User(
                                        binding.txtEmail.getText().toString().trim(),
                                        binding.txtPassword.getText().toString().trim(),
                                        binding.txtUsername.getText().toString().trim()
                                );
                                String uid = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(uid).setValue(user);

                                Toast.makeText(SignUpActivity.this, "Sign up successfull", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignUpActivity.this,"Enter Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}