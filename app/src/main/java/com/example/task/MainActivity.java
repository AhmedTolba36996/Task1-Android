package com.example.task;

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

public class MainActivity extends AppCompatActivity {

    EditText email , password ;
    Button login , register  ;

    String email_data , password_data ;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.emailLogin_edittext);
        password = findViewById(R.id.passwordLogin_edittext);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_data = email.getText().toString().trim();
                password_data = password.getText().toString().trim();

                if (TextUtils.isEmpty(email_data))
                {
                    email.setError("Email Is Required");
                    return;
                }
                if (TextUtils.isEmpty(password_data))
                {
                    password.setError("Password Is Required");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email_data , password_data)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "No Found", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this , RegisterActivity.class);
                startActivity(intent);

            }
        });




    }
}