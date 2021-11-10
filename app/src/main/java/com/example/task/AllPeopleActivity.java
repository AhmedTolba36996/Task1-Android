package com.example.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AllPeopleActivity extends AppCompatActivity {

    RecyclerView recyclerView ;
    FirebaseFirestore firestore ;
    FirestoreRecyclerAdapter madapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_people);

        recyclerView = findViewById(R.id.recycler);
        firestore = FirebaseFirestore.getInstance();

        // Query
        Query query = firestore.collection("users");

        // RecyclerView Option
        FirestoreRecyclerOptions<Model> options = new FirestoreRecyclerOptions.Builder<Model>()
                .setQuery(query, Model.class)
                .build();

         madapter = new FirestoreRecyclerAdapter<Model, UserViewHolder>(options) {
            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler , parent , false);
                return new UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Model model) {

                holder.nametxt.setText(model.getName());
                holder.emailtxt.setText(model.getEmail());
                holder.phonetxt.setText(model.getPhone());

            }
        };

         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         recyclerView.setAdapter(madapter);
         madapter.notifyDataSetChanged();


    }

    private class UserViewHolder extends RecyclerView.ViewHolder {

        TextView nametxt , phonetxt , emailtxt;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            nametxt = itemView.findViewById(R.id.item_name);
            phonetxt = itemView.findViewById(R.id.item_phone);
            emailtxt = itemView.findViewById(R.id.item_email);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        madapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        madapter.startListening();
    }
}