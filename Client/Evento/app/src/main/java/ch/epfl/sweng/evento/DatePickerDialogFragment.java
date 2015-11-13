package ch.epfl.sweng.evento;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by thomas on 09/11/15.
 */
public class DatePickerDialogFragment extends DialogFragment {

    private Calendar _date;
    private DatePickerDialog.OnDateSetListener _listener;

    public DatePickerDialogFragment()
    {
    }

    public void setDatePickerDialogFragment( Calendar date, DatePickerDialog.OnDateSetListener listener)
    {
        _date = date;
        _listener = listener;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedState)
    {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), _listener, _date.get(Calendar.YEAR), _date.get(Calendar.MONTH), _date.get(Calendar.DATE));
        return dialog;
    }

}