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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    EditText reg_name , reg_email , reg_pass , reg_phone ;
    Button reg_btn;

    String name , email , pass , phone ;
    String userID;

    FirebaseAuth mAuth;
    FirebaseFirestore firestore ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_name = findViewById(R.id.username);
        reg_email = findViewById(R.id.useremail);
        reg_pass = findViewById(R.id.userpassword);
        reg_phone = findViewById(R.id.userphone);
        reg_btn = findViewById(R.id.sign_up);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = reg_name.getText().toString().trim();
                email = reg_email.getText().toString().trim();
                pass = reg_pass.getText().toString().trim();
                phone = reg_phone.getText().toString().trim();

                if (TextUtils.isEmpty(name))
                {
                    reg_name.setError("Name Is Required");
                    return;
                }
                if (TextUtils.isEmpty(email))
                {
                    reg_email.setError("Email Is Required");
                    return;
                }
                if (TextUtils.isEmpty(pass))
                {
                    reg_pass.setError("Password Is Required");
                    return;
                }
                if (TextUtils.isEmpty(phone))
                {
                    reg_phone.setError("Phone Is Required");
                    return;
                }

                if(mAuth.getCurrentUser() !=null)
                {
                    startActivity(new Intent(getApplicationContext() , MainActivity.class));
                    finish();
                }

                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    userID = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = firestore.collection("users")
                                            .document(userID);

                                    // Upload Data **********************************
                                    Model model = new Model();
                                    model.setEmail(email);
                                    model.setName(name);
                                    model.setPhone(phone);

                                    documentReference.set(model).addOnSuccessListener( new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid)
                                        {
                                            Toast.makeText(RegisterActivity.this, "Created Profile for" + userID, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Toast.makeText(RegisterActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext() , ProfileActivity.class));

                                } else
                                {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });




            }
        });

    }
}