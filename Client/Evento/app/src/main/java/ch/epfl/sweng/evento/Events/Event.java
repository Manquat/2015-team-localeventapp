package ch.epfl.sweng.evento.Events;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.Set;

/**
 * Created by Val on 15.10.2015.
 */
public class Event implements ClusterItem {
    private final int mID;
    private final String mTitle;
    private final String mDescription;
    private final LatLng mLocation;
    private final String mAddress;
    private final String mCreator;//might be replaced by some kind of User class
    private final Set<String> mTags;
    private final Date mStartDate;
    private final Date mEndDate;

    public Event(int id,
                 String title,
                 String description,
                 double latitude,
                 double longitude,
                 String address,
                 String creator,
                 Set<String> tags,
                 Date startDate,
                 Date endDate) {
        mID = id;
        mTitle = title;
        mDescription = description;
        mLocation = new LatLng(latitude, longitude);
        mAddress = address;
        mCreator = creator;
        mTags = tags;
        mStartDate = new Date(startDate);
        mEndDate = new Date(endDate);
    }

    public Event(int id,
                 String title,
                 String description,
                 double latitude,
                 double longitude,
                 String address,
                 String creator,
                 Set<String> tags) {
        mID = id;
        mTitle = title;
        mDescription = description;
        mLocation = new LatLng(latitude, longitude);
        mAddress = address;
        mCreator = creator;
        mTags = tags;
        mStartDate = new Date();
        mEndDate = new Date();
    }

    public void debugLogEvent() {
        Log.d("Event " + mID + " : ", "title : " + mTitle);
    }

    public int getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getLatitude() {
        return mLocation.latitude;
    }

    public double getLongitude() {
        return mLocation.longitude;
    }

    public LatLng getLocation() {
        return mLocation;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getCreator() {
        return mCreator;
    }

    public Set<String> getTags() {
        return mTags;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    @Override
    public LatLng getPosition() {
        return mLocation;
    }

    /**
     * The signature of an Event is its Date in the long form to which its ID is appended.
     * It allows to order Events by starting Date AND by ID at the same time.
     * The ID is written on 6 digits for now.
     * @return the signature of the Event in the form yyyymmddhhmmID
     */
    public long getSignature() { return (100000 * getStartDate().toLong() + (long)getID());}

    public static class Date {
        private final int mYear;
        private final int mMonth;
        private final int mDay;
        private final int mHour;
        private final int mMinutes;

        public String toString() {
            return mYear + "/" + mMonth + "/" + mDay + "  " + mHour + ":" + mMinutes;
        }

        /**
         * This method returns a long representing the date with appended values
         * It makes comparison between 2 Dates trivial and is also used to get an Event's signature
         * @return the Date in the form yyyymmddhhmm
         */
        public long toLong() {
            return (long) (Math.pow(10,8)
                    * mYear + Math.pow(10,6)
                    * mMonth + Math.pow(10,4)
                    * mDay + Math.pow(10,2)
                    * mHour + mMinutes);
        }

        public Date() {
            mYear = 0;
            mMonth = 0;
            mDay = 0;
            mHour = 0;
            mMinutes = 0;
        }

        public Date(int year, int month, int day, int hour, int minutes) {
            mYear = year;
            mMonth = month;
            mDay = day;
            mHour = hour;
            mMinutes = minutes;
        }

        public Date(Date other) {
            mYear = other.mYear;
            mMonth = other.mMonth;
            mDay = other.mDay;
            mHour = other.mHour;
            mMinutes = other.mMinutes;
        }
    }

}


