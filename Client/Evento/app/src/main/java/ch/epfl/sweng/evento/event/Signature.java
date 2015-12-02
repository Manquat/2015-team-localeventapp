package ch.epfl.sweng.evento.event;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Class to represent the key of the EventSet. Unique for every event
 */
public class Signature implements Comparable<Signature> {
    private int mID;
    private Calendar mCalendar;

    public Signature(int ID, Calendar calendar) {
        mID = ID;

        // defensive copy
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(calendar.getTime());
    }

    @Override
    public int compareTo(@NonNull Signature another) {
        int result = mCalendar.compareTo(another.mCalendar);
        return (result == 0) ? ((Integer) mID).compareTo(another.mID) : result;
    }

    @Override
    public int hashCode() {
        return mID + 31 * mCalendar.hashCode();
    }

    @Override
    public boolean equals(Object objectToCompare) {
        if (objectToCompare == this) {
            return true;
        }
        if (objectToCompare == null || objectToCompare.getClass() != getClass()) {
            return false;
        }

        Signature signatureToCompare = (Signature) objectToCompare;
        return mID == signatureToCompare.mID &&
                mCalendar.equals(signatureToCompare.mCalendar);
    }
}
