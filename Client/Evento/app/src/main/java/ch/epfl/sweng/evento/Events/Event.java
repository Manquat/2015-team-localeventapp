package ch.epfl.sweng.evento.Events;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.ByteArrayOutputStream;
import java.util.Set;

/**
 * Created by Val on 15.10.2015.
 */
public class Event implements ClusterItem {
    private static final String TAG = "Event";
    private final int mID;
    private final String mTitle;
    private final String mDescription;
    private final LatLng mLocation;
    private final String mAddress;
    private final String mCreator;//might be replaced by some kind of User class
    private final Set<String> mTags;
    private final Date mStartDate;
    private final Date mEndDate;
    private String mPicture;

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
        mPicture = samplePicture();
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
        mPicture = "";
    }


    public void setPicture(String picture) {
        mPicture = picture;
    }

    /**
     * Converts the Bitmap passed in argument into a base64 String representing the image
     * and then stores it into the member mPicture
     * @param bitmap the bitmap image to be converted
     */
    public void setPicture(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] b = outputStream.toByteArray();
        mPicture = Base64.encodeToString(b, Base64.DEFAULT);
    }

    public void debugLogEvent() {
        Log.d(TAG, "Event " + mID + " : title : " + mTitle);
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

    public String getPictureAsString() {
        return mPicture;
    }

    /**
     * converts the String member named mPicture that represents a Bitmap image encoded in base64
     * into an actual Bitmap.
     * @return The Bitmap converted from mPicture
     */
    public Bitmap getPicture() {
        byte[] encodeByte = Base64.decode(mPicture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    @Override
    public LatLng getPosition() {
        return mLocation;
    }

    /**
     * The signature of an Event is its Date in the long form to which its ID is appended.
     * It allows to order Events by starting Date AND by ID at the same time.
     * The ID is written on 6 digits for now.
     *
     * @return the signature of the Event in the form yyyymmddhhmmID
     */
    public long getSignature() {
        return (100000 * getStartDate().toLong() + (long) getID());
    }

    //This is a temporary method to test if the server can handle very long strings
    public String samplePicture() {
        return "Qk2uFAAAAAAAAIoEAAB8AAAAxwAAAMcAAAABAAgAAQAAACQQAAASCwAAEgsAAAABAAAAAQAAAAD/ " +
                "AAD/AAD/AAAAAAAA/0JHUnMAAAAAAAAAAFS4HvwAAAAAAAAAAGZmZvwAAAAAAAAAAMT1KP8AAAAA " +
                "AAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAgAAAAICAAIAAAACAAIAAgIAAAMDAwABI " +
                "SEgAcHBwAABAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "BvsK+QMBAwAJAQMAE/wDAQ35AwUM/AMAEPsDAAMEOQAAAAkACvkD+wP5BvsD+Qb7CvkDAQMACQED " +
                "ABP8AwEN+QMFDPwDABD7AwADBDkAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMADfsDAAn8 " +
                "AwQzAAAABgAG+Rz7CvkDAAkBAwADBBD8AwQQ+QMEDPwDAA37AwAJ/AMEMwAAAAYABvkc+wr5AwAJ " +
                "AQMAAwQQ/AMEEPkDBAz8AwAN+wMACfwDBDMAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMA " +
                "DfsDAAn8AwQzAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJ " +
                "AQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8 " +
                "AwQtAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wD " +
                "AQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAA " +
                "CQAH+R/7AwAJAQMAAwQQ/AMEEPkP/AMADfsDABz8JgAAAAkAB/kf+wMACQEDAAMEEPwDBBD5D/wD " +
                "AA37AwAc/CYAAAAJAAf5H/sDAAkBAwADBBD8AwQQ+Q/8AwAN+wMAHPwmAAAACQAK+Rz7AwAJAQMA " +
                "ABP8AwEN+QMFDPwDABD7AwADBDkAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMADfsDAAn8 " +
                "AwQzAAAABgAG+Rz7CvkDAAkBAwADBBD8AwQQ+QMEDPwDAA37AwAJ/AMEMwAAAAYABvkc+wr5AwAJ " +
                "AQMAAwQQ/AMEEPkDBAz8AwAN+wMACfwDBDMAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMA " +
                "DfsDAAn8AwQzAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJ " +
                "AQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8 " +
                "AwQtAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wD " +
                "AQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAA " +
                "CQAH+R/7AwAJAQMAAwQQ/AMEEPkP/AMADfsDABz8JgAAAAkAB/kf+wMACQEDAAMEEPwDBBD5D/wD " +
                "AA37AwAc/CYAAAAJAAf5H/sDAAkBAwADBBD8AwQQ+Q/8AwAN+wMAHPwmAAAACQAK+Rz7AwAJAQMA " +
                "AwQzAAAABgAG+Rz7CvkDAAkBAwADBBD8AwQQ+QMEDPwDAA37AwAJ/AMEMwAAAAYABvkc+wr5AwAJ " +
                "AQMAAwQQ/AMEEPkDBAz8AwAN+wMACfwDBDMAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMA " +
                "DfsDAAn8AwQzAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJ " +
                "AQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8 " +
                "AwQtAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wD " +
                "AQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAA " +
                "CQAH+R/7AwAJAQMAAwQQ/AMEEPkP/AMADfsDABz8JgAAAAkAB/kf+wMACQEDAAMEEPwDBBD5D/wD " +
                "AA37AwAc/CYAAAAJAAf5H/sDAAkBAwADBBD8AwQQ+Q/8AwAN+wMAHPwmAAAACQAK+Rz7AwAJAQMA " +
                "AwQzAAAABgAG+Rz7CvkDAAkBAwADBBD8AwQQ+QMEDPwDAA37AwAJ/AMEMwAAAAYABvkc+wr5AwAJ " +
                "AQMAAwQQ/AMEEPkDBAz8AwAN+wMACfwDBDMAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMA " +
                "DfsDAAn8AwQzAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJ " +
                "AQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8 " +
                "AwQtAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wD " +
                "AQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAA " +
                "CQAH+R/7AwAJAQMAAwQQ/AMEEPkP/AMADfsDABz8JgAAAAkAB/kf+wMACQEDAAMEEPwDBBD5D/wD " +
                "AA37AwAc/CYAAAAJAAf5H/sDAAkBAwADBBD8AwQQ+Q/8AwAN+wMAHPwmAAAACQAK+Rz7AwAJAQMA " +
                "AAD/AAD/AAAAAAAA/0JHUnMAAAAAAAAAAFS4HvwAAAAAAAAAAGZmZvwAAAAAAAAAAMT1KP8AAAAA " +
                "AAAAAAAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAgAAAgAAAAICAAIAAAACAAIAAgIAAAMDAwABI " +
                "SEgAcHBwAABAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " +
                "BvsK+QMBAwAJAQMAE/wDAQ35AwUM/AMAEPsDAAMEOQAAAAkACvkD+wP5BvsD+Qb7CvkDAQMACQED " +
                "ABP8AwEN+QMFDPwDABD7AwADBDkAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMADfsDAAn8 " +
                "AwQzAAAABgAG+Rz7CvkDAAkBAwADBBD8AwQQ+QMEDPwDAA37AwAJ/AMEMwAAAAYABvkc+wr5AwAJ " +
                "AQMAAwQQ/AMEEPkDBAz8AwAN+wMACfwDBDMAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMA " +
                "DfsDAAn8AwQzAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJ " +
                "AQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8 " +
                "AwQtAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wD " +
                "AQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAA " +
                "CQAH+R/7AwAJAQMAAwQQ/AMEEPkP/AMADfsDABz8JgAAAAkAB/kf+wMACQEDAAMEEPwDBBD5D/wD " +
                "AA37AwAc/CYAAAAJAAf5H/sDAAkBAwADBBD8AwQQ+Q/8AwAN+wMAHPwmAAAACQAK+Rz7AwAJAQMA " +
                "ABP8AwEN+QMFDPwDABD7AwADBDkAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMADfsDAAn8 " +
                "AwQzAAAABgAG+Rz7CvkDAAkBAwADBBD8AwQQ+QMEDPwDAA37AwAJ/AMEMwAAAAYABvkc+wr5AwAJ " +
                "AQMAAwQQ/AMEEPkDBAz8AwAN+wMACfwDBDMAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMA " +
                "DfsDAAn8AwQzAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJ " +
                "AQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8 " +
                "AwQtAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wD " +
                "AQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAA " +
                "CQAH+R/7AwAJAQMAAwQQ/AMEEPkP/AMADfsDABz8JgAAAAkAB/kf+wMACQEDAAMEEPwDBBD5D/wD " +
                "AA37AwAc/CYAAAAJAAf5H/sDAAkBAwADBBD8AwQQ+Q/8AwAN+wMAHPwmAAAACQAK+Rz7AwAJAQMA " +
                "AwQzAAAABgAG+Rz7CvkDAAkBAwADBBD8AwQQ+QMEDPwDAA37AwAJ/AMEMwAAAAYABvkc+wr5AwAJ " +
                "AQMAAwQQ/AMEEPkDBAz8AwAN+wMACfwDBDMAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMA " +
                "DfsDAAn8AwQzAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJ " +
                "AQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8 " +
                "AwQtAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wD " +
                "AQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAA " +
                "CQAH+R/7AwAJAQMAAwQQ/AMEEPkP/AMADfsDABz8JgAAAAkAB/kf+wMACQEDAAMEEPwDBBD5D/wD " +
                "AA37AwAc/CYAAAAJAAf5H/sDAAkBAwADBBD8AwQQ+Q/8AwAN+wMAHPwmAAAACQAK+Rz7AwAJAQMA " +
                "AwQzAAAABgAG+Rz7CvkDAAkBAwADBBD8AwQQ+QMEDPwDAA37AwAJ/AMEMwAAAAYABvkc+wr5AwAJ " +
                "AQMAAwQQ/AMEEPkDBAz8AwAN+wMACfwDBDMAAAAGAAb5HPsK+QMACQEDAAMEEPwDBBD5AwQM/AMA " +
                "DfsDAAn8AwQzAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJ " +
                "AQMAE/wDBQ35AwEP/AMADfsDAA/8AwQtAAAABgAG+Sb7AwAJAQMAE/wDBQ35AwEP/AMADfsDAA/8 " +
                "AwQtAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wD " +
                "AQ35AwUM/AMAEPsDABL8BAQpAAAACQAD+Sb7AwAJAQMAE/wDAQ35AwUM/AMAEPsDABL8BAQpAAAA " +
                "yAAAAMgAAADIAAAAyAAAAMgAAADIAAAAyAAAAMgAAADIAAAAyAAAAMgAAADIAAAAAAE=";
    }

    public static class Date {
        private int mYear;
        private int mMonth;
        private int mDay;
        private int mHour;
        private int mMinutes;

        public String toString() {
            return mYear + "/" + mMonth + "/" + mDay + "  " + mHour + ":" + mMinutes;
        }

        /**
         * This method returns a long representing the date with appended values
         * It makes comparison between 2 Dates trivial and is also used to get an Event's signature
         *
         * @return the Date in the form yyyymmddhhmm
         */
        public long toLong() {
            return (long) (Math.pow(10, 8)
                    * mYear + Math.pow(10, 6)
                    * mMonth + Math.pow(10, 4)
                    * mDay + Math.pow(10, 2)
                    * mHour + mMinutes);
        }

        public Date() {
            mYear = 0;
            mMonth = 0;
            mDay = 0;
            mHour = 0;
            mMinutes = 0;
        }

        public void setTime(int hour, int minutes) {
            mHour = hour;
            mMinutes = minutes;
        }

        public int getYear() {
            return mYear;
        }

        public int getMonth() {
            return mMonth;
        }

        public int getDay() {
            return mDay;
        }

        public int getMinutes() {
            return mMinutes;
        }

        public int getHour() {
            return mHour;
        }


        public void setDate(int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;
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


