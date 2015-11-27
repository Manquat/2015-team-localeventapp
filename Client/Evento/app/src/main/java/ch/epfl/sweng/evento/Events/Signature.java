package ch.epfl.sweng.evento.Events;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Class to represent the key of the EventSet. Unique for every event
 */
public class Signature implements Comparable<Signature> {
    private int mID;
    private Calendar mCalendar;

    public Signature(int ID, Calendar calendar) {
        mID = ID;
        mCalendar = calendar;
    }


    public int getID() {
        return mID;
    }

    public Calendar getCalendar() {
        //deep copy
        return (Calendar) mCalendar.clone();
    }


    @Override
    public int compareTo(@NonNull Signature another) {
        int result = mCalendar.compareTo(another.getCalendar());
        return (result == 0) ? ((Integer) mID).compareTo(another.getID()) : result;
    }

    @Override
    public int hashCode() {
        return mID + 31*mCalendar.hashCode();
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
        return mID == signatureToCompare.getID() &&
                mCalendar.equals(signatureToCompare.getCalendar());
    }
}
