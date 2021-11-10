package com.example.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateDataActivity extends AppCompatActivity {

    EditText updateName , updatePhone , updateEmail;
    Button saveData;

    String name , phone , email ;

    FirebaseAuth firebaseAuth ;
    FirebaseFirestore firestore ;
    FirebaseUser firebaseUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        updateName = findViewById(R.id.update_name);
        updatePhone = findViewById(R.id.update_phone);
        updateEmail = findViewById(R.id.update_email);
        saveData = findViewById(R.id.save_data_changed);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser  =firebaseAuth.getCurrentUser();

        Intent data = getIntent();
        name = data.getStringExtra("name");
        phone = data.getStringExtra("phone");
        email = data.getStringExtra("email");

        updateName.setText(name);
        updatePhone.setText(phone);
        updateEmail.setText(email);

        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(updateName.getText().toString().isEmpty() || updatePhone.getText().toString().isEmpty() || updateEmail.getText().toString().isEmpty())
                {
                    Toast.makeText(UpdateDataActivity.this, "Fill Fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email_updated = updateEmail.getText().toString();
                firebaseUser.updateEmail(email_updated).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        DocumentReference documentReference = firestore.collection("users")
                                .document(firebaseUser.getUid());

                        // We Can Use Our Model Here instead Of HashMap
                        Map<String , Object> edited = new HashMap<>();
                        edited.put("email" , email_updated);
                        edited.put("name" , updateName.getText().toString());
                        edited.put("phone" , updatePhone.getText().toString());

                        documentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UpdateDataActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext() , ProfileActivity.class));
                            }
                        });

                        Toast.makeText(UpdateDataActivity.this, "Email Changed Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateDataActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



    }
}