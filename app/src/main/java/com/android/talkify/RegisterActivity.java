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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    // Widgets
    EditText userET,passET,emailET;
    Button registerBtn;

    // Firebase

    FirebaseAuth auth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialising Widgets
        userET=findViewById(R.id.userEditText);
        passET=findViewById(R.id.PassEditText);
        emailET=findViewById(R.id.emailEditText);
        registerBtn=findViewById(R.id.buttonRegister);

        //firebase auth
        auth = FirebaseAuth.getInstance();

        // adding event listener to button register
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_text = userET.getText().toString();
                String email_text = emailET.getText().toString();
                String password_text = passET.getText().toString();
                if(TextUtils.isEmpty(username_text)||TextUtils.isEmpty(email_text)||TextUtils.isEmpty(password_text)){
                    Toast.makeText(RegisterActivity.this,"Please Fill In All The Fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    RegisterNow(username_text, email_text,password_text);
                }
            }
        });
    }
    private void RegisterNow(final String username, String email,String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            myRef = FirebaseDatabase.getInstance()
                                    .getReference("MyUsers")
                                    .child(userid);
                            // hashmaps
                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");

                            // Opening The Main Activity After Succesfull Login
                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                            Toast.makeText(RegisterActivity.this,"User Registered Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Invalid Email or password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}