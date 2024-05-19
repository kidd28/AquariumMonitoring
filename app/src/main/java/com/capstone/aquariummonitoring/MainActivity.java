package com.capstone.aquariummonitoring;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView ntu, Date, Datetime,Status, Duration, Waiting;
    String NTU, status, DATE;
    RecyclerView rv;
    ArrayList<LogModel> logModelArrayList;
    FirebaseUser user;

    Button wifiButton,control;
    LinearLayout Dur, Wait;
    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);

        ntu = findViewById(R.id.ntu);
        Datetime = findViewById(R.id.time);
        Date = findViewById(R.id.Date);
        rv = findViewById(R.id.rv);
        wifiButton = findViewById(R.id.wifiButton);
        Status = findViewById(R.id.Status);
        control = findViewById(R.id.control);
        Dur = findViewById(R.id.Dur);
        Wait = findViewById(R.id.Wait);
        Duration = findViewById(R.id.Duration);
        Waiting = findViewById(R.id.Waiting);


        logModelArrayList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        rv.setLayoutManager(layoutManager);

        long millis = System.currentTimeMillis();

        java.sql.Date Cdate = new java.sql.Date(millis);
        System.out.println(Cdate);

        Dur.setVisibility(View.GONE);
        Wait.setVisibility(View.GONE);


        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ControlNotification.class));
            }
        });
        Date.setText(Cdate.toString());
        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WifiManager.class));

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NTU = "" + snapshot.child("Turbidity").getValue();
                ntu.setText(NTU);
                if(Integer.parseInt(NTU)>30){
                    sendNotification( "Click to see turbidity level and make action.","Too high turbidity");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Turbidity");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status = "" + snapshot.child("Status").getValue();
                Status.setText(status);
                if(status.equals("Active")){
                    Dur.setVisibility(View.VISIBLE);
                    Wait.setVisibility(View.GONE);
                    getCurrentcountdown();
                   /* DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("CurrentDrain");
                    reference4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String duration = ""+snapshot.child("Duration").getValue();
                            String state = ""+snapshot.child("State").getValue();

                            long endTime = System.currentTimeMillis() + (Long.parseLong(duration)* 1000);

                            if(state.equals("Started")){
                                getCurrentcountdown();
                            }else {
                                reference4.child("EndTime").setValue(endTime);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/
                }else if(status.equals("Idle")){
                    Wait.setVisibility(View.VISIBLE);
                    Dur.setVisibility(View.GONE);
                    showWaiting(43200000);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logModelArrayList.clear();
        loadLogs();
    }

    private void getCurrentcountdown() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CurrentDrain");
        reference.child("EndTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long endTime = dataSnapshot.getValue(Long.class);
                long currentTime = System.currentTimeMillis();
                long timeLeft = endTime - currentTime;

                startCountdown(timeLeft);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

    }

    private void startCountdown(long timeLeft) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;

                seconds = seconds % 60;
                minutes = minutes % 60;

                Duration.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }

            public void onFinish() {
                Duration.setText("00:00:00");
            }
        }.start();
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

    private void showWaiting(int milliseconds){
        new CountDownTimer(milliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                Waiting.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                Waiting.setText("00:00:00");
            }
        }.start();
    }
    private void showDuration(int milliseconds) {
        new CountDownTimer(milliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                Duration.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                Duration.setText("00:00:00");
            }
        }.start();
    }

    public void sendNotification (String message, String title ){

        Intent intent = new Intent(getApplicationContext(), ControlNotification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_IMMUTABLE);
        String channelId = "some_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
//                        .setContentTitle(getString(R.string.app_name)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}