package com.brainiac.mocae.needhelp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import java.util.Calendar;

/**
 * Created by mocae on 1/25/2017.
 */

public class DatePickerFragment extends DialogFragment
                    implements DatePickerDialog.OnDateSetListener {

    private OnDateSet _handlerOnSetDate;

    public void SetOnDateHandler(OnDateSet onDateSet) {
        _handlerOnSetDate = onDateSet;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        if (_handlerOnSetDate != null) {
            _handlerOnSetDate.OnNewDateSet(year, month + 1, dayOfMonth);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
}

interface OnDateSet {
    public void OnNewDateSet(int y, int m, int d);
}
