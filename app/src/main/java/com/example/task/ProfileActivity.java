package com.example.task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileActivity extends AppCompatActivity {

    TextView name , mail , phone ;
    Button update , allPeople , logout;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profile_name);
        mail = findViewById(R.id.profile_email);
        phone = findViewById(R.id.profile_Phone);

        update = findViewById(R.id.update_data);
        allPeople = findViewById(R.id.display_allpeople);
        logout = findViewById(R.id.log_out);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firestore.collection("users")
                .document(userID);
      documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
          @Override
          public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                name.setText(value.getString("name"));
                phone.setText(value.getString("phone"));
                mail.setText(value.getString("email"));

          }
      });

        allPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , AllPeopleActivity.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , UpdateDataActivity.class);

                intent.putExtra("name" , name.getText().toString());
                intent.putExtra("phone" , phone.getText().toString());
                intent.putExtra("email" , mail.getText().toString());


                startActivity(intent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext() , MainActivity.class));
              //  finish();
            }
        });

    }
}