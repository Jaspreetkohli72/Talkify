package com.android.talkify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Login_Activity extends AppCompatActivity {

    EditText userETLogin,passETLogin;
    Button loginBtn,registerNowBtn;

    // firebase
    FirebaseAuth auth;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        //checking for users existance:save current user

        if (firebaseUser != null) {
            Intent i = new Intent(Login_Activity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);



        userETLogin=findViewById(R.id.usernameEditText);
        passETLogin=findViewById(R.id.passwordEditText);
        loginBtn=findViewById(R.id.buttonLogin);
        registerNowBtn=findViewById(R.id.buttonRegisterNew);

        //firebase auth
        auth = FirebaseAuth.getInstance();

        //Register button
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Activity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        //login btn
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = userETLogin.getText().toString();
                String pass_text= passETLogin.getText().toString();

                // checking for empty
                if(TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)){
                    Toast.makeText(Login_Activity.this, "Please fill in all the fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(email_text,pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent i  =new Intent(Login_Activity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(Login_Activity.this, "Login Failed! Incorrect username or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}