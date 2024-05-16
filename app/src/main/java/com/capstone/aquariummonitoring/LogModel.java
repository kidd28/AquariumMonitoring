package com.capstone.aquariummonitoring;

import org.checkerframework.checker.units.qual.A;

public class LogModel {

    String Average_Turbidity, DateTime, Duration;

    public LogModel(){}
    public LogModel(String Average_Turbidity, String DateTime, String Duration){
        this.Average_Turbidity = Average_Turbidity;
        this.DateTime = DateTime;
        this.Duration = Duration;
    }

    public String getAverage_Turbidity() {
        return Average_Turbidity;
    }

    public void setAverage_Turbidity(String average_Turbidity) {
        Average_Turbidity = average_Turbidity;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }
}
