package com.capstone.aquariummonitoring;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.SpinnerAdapter;
import androidx.appcompat.widget.AppCompatSpinner;

public class CustomSpinner extends AppCompatSpinner {

    public CustomSpinner(Context context) {
        super(context);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, SpinnerAdapter adapter) {
        super(context);
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        super.setAdapter(adapter);
        setDropDownVerticalOffset(calculateOffset());
    }

    private int calculateOffset() {
        // Set the desired height here (e.g., 500 pixels)
        int desiredHeight = 250;
        return desiredHeight;
    }
}