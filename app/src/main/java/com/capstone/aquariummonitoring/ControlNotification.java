package com.capstone.aquariummonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ControlNotification extends AppCompatActivity {

    TextView ntu, Date, Datetime,Status,message;
    Button activate, stop;

    String status,NTU,ForeceAvtive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_notification);

        ntu = findViewById(R.id.ntu);
        Datetime = findViewById(R.id.time);
        Date = findViewById(R.id.Date);
        Status = findViewById(R.id.Status);
        message = findViewById(R.id.message);
        activate = findViewById(R.id.activate);
        stop = findViewById(R.id.stop);

        long millis = System.currentTimeMillis();
        java.sql.Date Cdate = new java.sql.Date(millis);
        Date.setText(Cdate.toString());

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Turbidity");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status = "" + snapshot.child("Status").getValue();
                Status.setText(status);

                if(status.equals("Active")){
                    stop.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                    activate.setVisibility(View.GONE);
                }else if (status.equals("Idle")){
                    activate.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                    stop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ForceActivate");
                reference.child("Activate").setValue("Yes").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Turbidity");
                        reference1.child("Status").setValue("Active");
                        message.setVisibility(View.GONE);
                        activate.setVisibility(View.GONE);
                        stop.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ForceActivate");
                reference.child("Activate").setValue("Stop").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Turbidity");
                        reference1.child("Status").setValue("Idle");
                        message.setVisibility(View.GONE);
                        activate.setVisibility(View.VISIBLE);
                        stop.setVisibility(View.GONE);

                    }
                });
            }
        });

    }
}