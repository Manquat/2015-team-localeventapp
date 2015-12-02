package ch.epfl.sweng.evento.Events;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.util.HashSet;
import java.util.Locale;

import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.User;


/**
 * The event class that holds all the information related to the event :
 * date of beginning, date of end, title, owner, .....
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
    private Calendar mStartDate;
    private Calendar mEndDate;
    private String mPicture;
    private Set<User> mParticipants;
    private final int mNumberMaxOfParticipants;



    public Event( int id,
                  String title,
                  String description,
                  double latitude,
                  double longitude,
                  String address,
                  String creator,
                  Set<String> tags,
                  String image,
                  Set<User> participants) {
        mID = id;
        mTitle = title;
        mDescription = description;
        mLocation = new LatLng(latitude, longitude);
        mAddress = address;
        mCreator = creator;
        mTags = tags;

        mStartDate = new GregorianCalendar();
        mEndDate = new GregorianCalendar();
        mPicture = samplePicture();
        mNumberMaxOfParticipants =  10;//TODO
        mParticipants = new HashSet<User>(participants);

    }

    public Event(int id,
                 String title,
                 String description,
                 double latitude,
                 double longitude,
                 String address,
                 String creator,
                 Set<String> tags,
                 Calendar startDate,
                 Calendar endDate,
                 Bitmap picture) {
        this(id, title, description, latitude, longitude, address, creator, tags, startDate, endDate);
        mStartDate = startDate;
        mEndDate = endDate;
        setPicture(picture);
    }

    public Event(int id,
                 String title,
                 String description,
                 double latitude,
                 double longitude,
                 String address,
                 String creator,
                 Set<String> tags,
                 Calendar startDate,
                 Calendar endDate) {
        this(id, title, description, latitude, longitude, address, creator, tags, "image", new HashSet<User>());
        mStartDate = startDate;
        mEndDate = endDate;
        mPicture = samplePicture();
    }

    /**
     * Easy way to print a event in a log
     * Not equivalent to serialized event (RestApi.Serializer) which provide string event acceptable
     * for the server
     */
    public String toString() {
        return this.getTitle() + ", " + this.getDescription() + ", " + this.getAddress()
                + ", (" + Double.toString(this.getLatitude()) + ", " + Double.toString(this.getLongitude())
                + "), " + this.getCreator() + ", (" + this.getProperDateString();
    }


    public boolean addParticipant(User participant){
        if (mParticipants.size() < mNumberMaxOfParticipants) {
            if(Build.VERSION.SDK_INT >= 19) {
                mParticipants.add(Objects.requireNonNull(participant, "Cannot add a null participant"));
            } else {
                if (participant != null){
                    mParticipants.add(participant);
                } else {
                    throw new NullPointerException("Cannot add a null participant");
                }
            }
            return true;
        } else {
            return false;
        }
    }

    
    public Set<User> getAllParticipant() {
        return mParticipants;
    }

    public int getMaxNumberOfParticipant() {
        return mNumberMaxOfParticipants;
    }

    public void removeParticipant(User participant){
        if(Build.VERSION.SDK_INT >= 19){
            if (checkIfParticipantIsIn(Objects.requireNonNull(participant, "Cannot remove a null participant"))) {
                mParticipants.remove(participant);
            }
        } else {
            if (checkIfParticipantIsIn(participant)) {
                mParticipants.remove(participant);
            }
        }
    }

    private boolean checkIfParticipantIsIn(User participant){
        return mParticipants.contains(participant);//Throw a nullPointerExc. if participant is null
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public void setPicture(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] b = outputStream.toByteArray();
            mPicture = Base64.encodeToString(b, Base64.DEFAULT);
        } else {
            mPicture = "";
        }

    }

    public boolean isFull(){
        if(mParticipants.size() >= mNumberMaxOfParticipants) return true;
        else return false;
    }


    public String getProperDateString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
        timeFormat.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        return timeFormat.format(mStartDate.getTime());
    }

    public static String asNiceString(Calendar calendar) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return dateFormat.format(calendar.getTime());
    }

    public String getStartDateAsString() {
        return asNiceString(mStartDate);
    }

    public String getEndDateAsString() {
        return asNiceString(mEndDate);
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

    public String getTagsString() {
        if (mTags.contains("Foot!") ||
                mTags.contains("Football")) {
            return "Football";
        } else if (mTags.contains("Basketball")) {
            return "Basketball";
        }
        else {
            return "Unknown";
        }
    }

    public Set<String> getTags() {
        return mTags;
    }

    public Calendar getStartDate() {
        return mStartDate;
    }

    public Calendar getEndDate() {
        return mEndDate;
    }

    public String getPictureAsString() {
        return mPicture;
    }

    /**
     * converts the String member named mPicture that represents a Bitmap image encoded in base64
     * into an actual Bitmap.
     *
     * @return The Bitmap converted from mPicture
     */
    public Bitmap getPicture() {

        byte[] encodeByte = Base64.decode(mPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
    }

    @Override
    public LatLng getPosition() {
        return mLocation;
    }

    //This is a temporary method to test if the server can handle very long strings
    public static String samplePicture() {
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
}


