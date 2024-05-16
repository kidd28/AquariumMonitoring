package com.capstone.aquariummonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView ntu, Date, Datetime;
    String NTU, TIME, DATE;
    RecyclerView rv;
    ArrayList<LogModel> logModelArrayList;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);

        ntu = findViewById(R.id.ntu);
        Datetime = findViewById(R.id.time);
        Date = findViewById(R.id.Date);
        rv = findViewById(R.id.rv);

        logModelArrayList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        rv.setLayoutManager(layoutManager);

        long millis = System.currentTimeMillis();

        java.sql.Date Cdate = new java.sql.Date(millis);
        System.out.println(Cdate);


        Date.setText(Cdate.toString());


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

        logModelArrayList.clear();
        loadLogs();

    }

    private void loadLogs() {
        logModelArrayList.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Logs");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logModelArrayList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    LogModel model = snap.getValue(LogModel.class);
                    logModelArrayList.add(0,model);
                }
                LogAdapter logAdapter = new LogAdapter(MainActivity.this, logModelArrayList);
                rv.setAdapter(logAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}