package com.example.astroweather;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;

public class CustomSpinner extends androidx.appcompat.widget.AppCompatSpinner {
    AdapterView.OnItemSelectedListener listener;

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(this, getSelectedView(), position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
    }
}
