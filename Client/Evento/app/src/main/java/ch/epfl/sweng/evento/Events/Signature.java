package ch.epfl.sweng.evento.Events;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by Tago on 26/11/2015.
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
            int compareTo = lhs.getCalendar().compareTo(rhs.getCalendar());

            // same date
            if (compareTo == 0) {
                if (lhs.getID() > rhs.getID()) {
                    // greater
                    return 1;
                } else if (lhs.getID() < rhs.getID()) {
                    // smaller
                    return -1;
                }
                else {
                    // the same
                    return 0;
                }
            }
            return compareTo;
        }
        else {
            if (lhs == null && rhs != null) {
                // by default null is the smallest possible
                return -1;
            }
            else if (lhs != null) {
                // by default null is the smallest possible
                return 1;
            }
            else {
                //the same null
                return 0;
            }
        }
    }
}
