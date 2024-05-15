package com.capstone.aquariummonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
TextView ntu, time,date;
String NTU, TIME, DATE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(MainActivity.this);

        ntu = findViewById(R.id.ntu);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NTU = "" + snapshot.child("Turbidity").getValue();
                ntu.setText(NTU);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}