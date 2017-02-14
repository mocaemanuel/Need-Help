package com.brainiac.mocae.needhelp;

import android.app.TimePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import java.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;

/**
 * Created by mocae on 1/25/2017.
 */

public class TimePickerFragment extends DialogFragment
                            implements TimePickerDialog.OnTimeSetListener {
    private OnTimeSet _handlerOnSetTime;

    public void SetOnTimeHandler(OnTimeSet onTimeSet) {
        _handlerOnSetTime = onTimeSet;
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hour, int minute) {
        if (_handlerOnSetTime != null) {
            _handlerOnSetTime.OnNewTimeSet(hour, minute);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}

interface OnTimeSet {
    public void OnNewTimeSet(int h, int m);
}

