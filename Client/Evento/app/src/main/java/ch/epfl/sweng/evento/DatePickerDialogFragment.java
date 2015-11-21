package ch.epfl.sweng.evento;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by thomas on 09/11/15.
 */
public class DatePickerDialogFragment extends DialogFragment {
    private String TAG = "DatePickerDialogFragment";
    private DatePickerDialog.OnDateSetListener listener;

    public DatePickerDialogFragment() {
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(listener == null) {
            Log.e(TAG, "listener is null");
        }
        // Use the current date as the default date in the picker
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

}