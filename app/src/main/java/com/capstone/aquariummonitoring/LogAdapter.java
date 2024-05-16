package com.capstone.aquariummonitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.HolderAdapter> {
    Context context;
    ArrayList<LogModel> logModelArrayList;
    FirebaseUser user;

    public LogAdapter(Context context, ArrayList<LogModel> model) {
        this.context = context;
        this.logModelArrayList = model;
    }

    @NonNull
    @Override
    public LogAdapter.HolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_layout, parent, false);
        return new LogAdapter.HolderAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.HolderAdapter holder, int position) {
        LogModel logmodel = logModelArrayList.get(position);
        String aveTurbidity = logmodel.getAverage_Turbidity();
        String DateTime = logmodel.DateTime;
        String SDuration = logmodel.Duration;
        String Duration = convertSeconds(Integer.valueOf(SDuration));


        holder.ntu.setText(aveTurbidity);
        holder.DateTime.setText(DateTime);
        holder.Duration.setText(Duration);

    }

    @Override
    public int getItemCount() {
        return logModelArrayList.size();
    }

    public class HolderAdapter extends RecyclerView.ViewHolder {

        TextView ntu, DateTime,Duration;
        public HolderAdapter(@NonNull View itemView) {
            super(itemView);

            ntu= itemView.findViewById(R.id.ntu);
            DateTime= itemView.findViewById(R.id.DateTime);
            Duration= itemView.findViewById(R.id.Duration);


        }
    }

    public static String convertSeconds(int seconds) {
        int h = seconds/ 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        String sh = (h > 0 ? String.valueOf(h) + " " + "h" : "");
        String sm = (m < 10 && m > 0 && h > 0 ? "0" : "") + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : String.valueOf(m) + " " + "min") : "");
        String ss = (s == 0 && (h > 0 || m > 0) ? "" : (s < 10 && (h > 0 || m > 0) ? "0" : "") + String.valueOf(s) + " " + "sec");
        return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "") + ss;
    }
}
