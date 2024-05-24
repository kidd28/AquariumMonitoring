package com.capstone.aquariummonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ControlNotification extends AppCompatActivity {

    TextView ntu, Date, Datetime,Status,message;
    Button activate, stop,save;
    Boolean ForceStart ;
    String status,NTU,ForeceActive;
    Spinner spinner,spinner1;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_SPINNER_POSITION = "SpinnerPosition";
    private static final String PREF_SPINNER_POSITION1 = "SpinnerPosition1";
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

        message.setVisibility(View.GONE);
        activate.setVisibility(View.GONE);
        stop.setVisibility(View.GONE);

        spinner = findViewById(R.id.spinner);
        spinner1 = findViewById(R.id.spinner1);
        ForceStart = false;
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Turbidity");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status = "" + snapshot.child("Status").getValue();
                Status.setText(status);
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("ForceActivate");
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ForeceActive = "" + snapshot.child("Activate").getValue();

                        if(status.equals("Idle")&& ForeceActive.equals("No")){
                            message.setVisibility(View.GONE);
                            activate.setVisibility(View.VISIBLE);
                            stop.setVisibility(View.GONE);
                        }else if (status.equals("Idle")&& ForeceActive.equals("Stop") ) {
                            message.setVisibility(View.GONE);
                            activate.setVisibility(View.VISIBLE);
                            stop.setVisibility(View.GONE);
                        }
                        else if (status.equals("Active")&& ForeceActive.equals("Yes") ) {
                            message.setVisibility(View.GONE);
                            activate.setVisibility(View.GONE);
                            stop.setVisibility(View.VISIBLE);
                        }else if (status.equals("Active")&& ForeceActive.equals("No") ) {
                            message.setVisibility(View.VISIBLE);
                            activate.setVisibility(View.GONE);
                            stop.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayList<String> values = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            values.add(String.valueOf(i)+" hour(s)");
        }

        ArrayList<String> values1 = new ArrayList<>();
        for (int i = 1; i <= 18; i++) {
            values1.add(String.valueOf(i)+" Liter(s)");
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values1);


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner1.setAdapter(adapter1);

        // Retrieve the saved spinner position from SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int spinnerPosition = settings.getInt(PREF_SPINNER_POSITION, 0);
        int spinnerPosition1 = settings.getInt(PREF_SPINNER_POSITION1, 0);
        spinner.setSelection(spinnerPosition);
        spinner1.setSelection(spinnerPosition1);


        // Set an item selected listener to get the selected value
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Save the selected spinner position to SharedPreferences
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(PREF_SPINNER_POSITION, position);
                editor.apply();



                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("NextTimeInterval");
                reference.child("Hours").setValue(position+1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected if necessary
            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Save the selected spinner position to SharedPreferences
                SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor1 = settings1.edit();
                editor1.putInt(PREF_SPINNER_POSITION1, position);
                editor1.apply();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("AquariumSize");
                reference.child("Liter").setValue(position+1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected if necessary
            }
        });

        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlNotification.this,MainActivity.class));
                finish();
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
                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ControlNotification.this,MainActivity.class));
        finish();
    }
}