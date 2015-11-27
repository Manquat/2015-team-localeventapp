package ch.epfl.sweng.evento.Events;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Class to represent the key of the EventSet. Unique for every event
 */
public class Signature implements Comparable<Signature>, Comparator<Signature> {
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
        return compare(this, another);
    }

    @Override
    public int compare(Signature lhs, Signature rhs) {
        if (lhs != null && rhs != null) {
            int result = lhs.getCalendar().compareTo(rhs.getCalendar());
            return (result == 0) ? ((Integer) lhs.getID()).compareTo(rhs.getID()) : result;
        } else {
            if (lhs == null && rhs != null) {
                // by default null is the smallest possible
                return -1;
            } else if (lhs != null) {
                // by default null is the smallest possible
                return 1;
            } else {
                //the same null
                return 0;
            }
        }
    }
}
