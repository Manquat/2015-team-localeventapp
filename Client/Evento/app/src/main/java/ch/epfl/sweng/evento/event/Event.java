package ch.epfl.sweng.evento.event;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

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
    private final int mCreatorId;//might be replaced by some kind of User class
    private final Set<String> mTags;
    private final int mNumberMaxOfParticipants;
    private Calendar mStartDate;
    private Calendar mEndDate;
    private String mPicture;
    private Set<User> mParticipants;


    public Event(int id,
                 String title,
                 String description,
                 double latitude,
                 double longitude,
                 String address,
                 int creatorId,
                 Set<String> tags,
                 Calendar startDate,
                 Calendar endDate,
                 String image,
                 Set<User> participants) {
        mID = id;
        mTitle = title;
        mDescription = description;
        mLocation = new LatLng(latitude, longitude);
        mAddress = address;
        mCreatorId = creatorId;
        mTags = tags;
        mStartDate = startDate;
        mEndDate = endDate;
        mPicture = image;
        mNumberMaxOfParticipants = 10;//TODO
        mParticipants = participants;

    }

    public Event(int id, String title, String description, double latitude, double longitude,
                 String address, int creator, Set<String> tags, Calendar startDate,
                 Calendar endDate, Bitmap picture) {
        this(id, title, description, latitude, longitude, address, creator, tags,
                startDate, endDate, bitmapToString(picture), new HashSet<User>());
    }

    public Event(int id, String title, String description, double latitude, double longitude,
                 String address, int creator, Set<String> tags, Calendar startDate,
                 Calendar endDate) {
        this(id, title, description, latitude, longitude, address, creator, tags,
                startDate, endDate, samplePicture(), new HashSet<User>());
    }

    public Event(int id, String title, String description, double latitude, double longitude,
                 String address, int creator, Set<String> tags, String image, Set<User> users) {
        this(id, title, description, latitude, longitude, address, creator, tags,
                new GregorianCalendar(), new GregorianCalendar(), image, users);

    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT).replace(System.getProperty("line.separator"), "");
    }

    public static String asNiceString(Calendar calendar) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        return dateFormat.format(calendar.getTime());
    }

    //This is a temporary method to test if the server can handle very long strings
    public static String samplePicture() {
        return "Qk1mKgAAAAAAADYAAAAoAAAAPAAAADwAAAABABgAAAAAADAqAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AAAAAAAASIT/SIT/SIT/k8bpk8bpk8bpk8bpk8Xnk8bplcjrk8Xnk8bpk8bpk8bpk8bpSIT/ SIT/SIT/AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AP//AAAAAAAASIT/SIT/SIT/lcjrk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bp k8bpk8bpk8bplcjrSIT/SIT/SIT/AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AAAAAAAASIT/SIT/SIT/k8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8Xnk8Xnlcjr k8Xnk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpSIT/SIT/SIT/AAAAAAAAAP//AP//AP//AP//AP// AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AP//AAAAAAAASIT/SIT/SIT/k8bpk8bpk8bpk8XnjsHlk8bpk8bpk8bplcjrlcjr k8bpk8Xnk8bpk8bplcjrlcjrk8bpk8bpk8bpjsHlk8Xnk8bpk8bpk8bpSIT/SIT/SIT/AAAAAAAA AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP// AP//AP//AP//AP//AP//AP//AAAASIT/SIT/SIT/k8bpk8bpk8bpk8bpk8bpicPqjsHlk8Xnk8bp lcjrlcjrlcjrk8bpk8Xnk8bpk8bplcjrlcjrlcjrk8bpk8XnjsHlicPqk8bpk8bpk8bpk8bpk8bp SIT/SIT/SIT/AAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP// AP//AP//AP//AP//AP//AP//AP//AP//AAAASIT/SIT/lcjrk8bpk8bpk8bpk8bpk8bpk8bpk8bp k8bpk8bpAAAAAAAAAAAAAAAAAAAAk8bpk8bpAAAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bp k8bpk8bpk8bpk8bplcjrSIT/SIT/AAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AP//AP// AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAASIT/SIT/k8bpk8bpk8bpk8bpk8bpk8bp lcjrk8bpk8bplcjrAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlcjr k8bpk8bplcjrk8bpk8bpk8bpk8bpk8bpk8bpSIT/SIT/AAAAAP//AP//AP//AP//AP//AP//AP// AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAASIT/k8Xnk8Xnlcjrk8bp k8bpk8XnlcjrAAAAAAAAAAAAAAAAAAAABAULAAAAAAAAAAAABAULAAAAAAAABAULAAAAAAAAAAAA BAULAAAAAAAAAAAAAAAAAAAAlcjrk8Xnk8bpk8bplcjrk8Xnk8XnSIT/AAAAAP//AP//AP//AP// AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAASIT/k8bp lcjrk8Xnlcjrlcjrk8bpAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAk8bplcjrlcjrk8Xnlcjrk8bpSIT/AAAAAP// AP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAA SIT/SIT/k8bpk8bpk8bpk8bpicPqAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAicPqk8bpk8bpk8bpk8bp SIT/SIT/AAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP// AP//AP//AAAASIT/lcjrk8bpk8bplcjrlcjrk8bpAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA SIT/SIT/SIT/SIT/SIT/SIT/SIT/SIT/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAk8bplcjr lcjrk8bpk8bplcjrSIT/AAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP// AP//AP//AAAAAAAAAAAASIT/SIT/lcjrk8bpk8bpicPqk8bpk8bpAAAAAAAAAAAAAAAAAAAAAAAA AAAASIT/SIT/SIT/k8bpk8bpk8bpk8bpk8bpk8bpSIT/SIT/SIT/AAAAAAAAAAAAAAAAAAAAAAAA AAAAk8bpk8bpicPqk8bpk8bplcjrSIT/SIT/AAAAAAAAAAAAAP//AP//AP//AP//AP//AP//AAAA AAAAAP//AP//AAAAAAAASIT/SIT/AAAASIT/k8bpk8Xnk8bpk8bpk8bpAAAAAAAAAAAAAAAAAAAA AAAAAAAABAULSIT/SIT/k8bpk8bplcjrk8bpk8bpicPqk8bplcjrk8bpk8bpSIT/SIT/BAULAAAA AAAAAAAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8Xnk8bpSIT/AAAASIT/SIT/AAAAAAAAAP//AP// AP//AP//AAAAAAAAAP//AAAASIT/SIT/SIT/jsHlAAAASIT/k8bpk8bpk8bpk8bpk8bpAAAAAAAA AAAAAAAAAAAAAAAAAAAASIT/SIT/lcjricPqk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpicPqlcjr SIT/SIT/AAAAAAAAAAAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bpSIT/AAAAjsHlSIT/SIT/ SIT/AAAAAP//AP//AP//AAAAAAAAAAAASIT/SIT/k8bpjsHljsHlAAAASIT/k8bpk8bpk8bpk8bp k8bpBAULAAAAAAAAAAAAk8bpk8bpAAAASIT/lcjrlcjrk8XnjsHlicPqk8bpk8bpk8bpk8bpicPq jsHlk8XnlcjrlcjrSIT/AAAAk8bpk8bpAAAAAAAAAAAABAULk8bpk8bpk8bpk8bpk8bpSIT/AAAA jsHljsHlk8bpSIT/SIT/AAAAAP//AP//AAAAAAAAAAAASIT/jsHlk8bpjsHljsHlAAAASIT/k8bp k8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAk8bpk8bpAAAASIT/k8bpk8Xnlcjrlcjrk8bpk8bplcjr k8Xnk8bpk8bplcjrlcjrk8Xnk8bpSIT/AAAAk8bpk8bpAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bp k8bpSIT/AAAAjsHljsHlk8bpjsHlSIT/AAAAAP//AP//AAAAAAAAAAAASIT/jsHlk8bpjsHljsHl AAAASIT/k8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAk8bpk8bpk8bpAAAASIT/k8bpk8bpk8bpk8Xn k8bpk8bpnMfjk8Xnk8bpk8bpk8Xnk8bpk8bpk8bpSIT/AAAAk8bpk8bpk8bpAAAAAAAAAAAAk8bp k8bpk8bpk8bpk8bpSIT/AAAAk8bpjsHljsHljsHlSIT/SIT/AAAAAP//AAAAAAAASIT/SIT/jsHl jsHljsHlk8bpAAAASIT/k8bpk8bpk8bpk8bpk8bpAAAAAAAAk8bpk8bpk8bpk8bpAAAASIT/k8bp k8bpk8Xnk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8Xnk8bpk8bpSIT/AAAAk8bpk8bpk8bpk8bp AAAAAAAAk8bpk8bpk8bpk8bpk8bpSIT/AAAAlcjrk8bpjsHlk8bpk8bpSIT/AAAAAP//AAAAAAAA SIT/k8bpk8bpjsHlk8bplcjrAAAASIT/k8bpk8bpk8bpk8bpk8bpAAAAAAAAk8bpk8bpk8bpk8bp AAAAAAAAk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpAAAAAAAAk8bp k8bpk8bpk8bpAAAAAAAAk8bpk8bpAAAAAAAAAAAAAAAAAAAAk8bpjsHljsHljsHljsHlSIT/SIT/ AAAAAAAAAAAASIT/jsHljsHljsHljsHlk8bpAAAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bp k8bpk8bpAAAAwMDAAAAAAAAAAAAAk8bpk8bpnMnok8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bpAAAA AAAAwMDAAAAAk8bpk8bpk8bpAAAAk8bpk8bpAAAAACNGACNGACNGACNGAAAAjsHljsHljsHlk8bp k8XnSIT/SIT/AAAAAAAAAAAASIT/k8Xnk8bpjsHljsHljsHlAAAAACNGACNGACNGACNGAAAAk8bp k8bpnMnolcjrk8bpAAAAwMDAwMDA////AAAAgAAAAAAAk8bpk8bpk8bpk8bpk8bpk8bpk8bpk8bp k8bpAAAAAACAAAAAwMDAwMDAAAAAk8bpk8bpk8bpk8bpAAAAHjZhHjZhHjZhHjZhACNGAAAAAAAA jsHljsHljsHljsHlSIT/AAAAAP//AAAAAAAASIT/jsHljsHljsHljsHlAAAAAAAAACNGHjZhHjZh HjZhHjZhAAAAk8bplcjrnMnoAAAAwMDAwMDA////////AAAAgAAAgAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAACAAACAAAAA////wMDAwMDAAAAAk8bpk8bpk8bpAAAAAAAAHjZhHjZhHjZh ACNGAAAASIT/AAAAAAAAAAAASIT/SIT/AAAAAP//AAAAAAAASIT/SIT/AAAAAAAAAAAASIT/AAAA ACNGHjZhHjZhHjZhAAAAAAAAk8bplcjrlcjrAAAAwMDA////////AAAAgAAAAAAAAAAAAAAAAAAA k8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAAACAAAAA////wMDAAAAAk8bpk8bpk8bpk8bpAAAA HjZhHjZhHjZhACNGAAAASIT/SIT/jsHljsHlSIT/AAAAAP//AP//AAAAAAAAAAAASIT/jsHljsHl SIT/SIT/AAAAACNGHjZhHjZhHjZhAAAAk8bpk8bpk8bpjsHlAAAAwMDA////////AAAA/wAAAAAA AAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAAAD/AAAA////wMDAAAAAk8bplcjr k8bpk8bpk8bpAAAAAAAAHjZhACNGAAAAAAAASIT/SIT/SIT/SIT/AAAAAP//AP//AAAAAAAAAAAA SIT/SIT/SIT/SIT/AAAAAAAAACNGHjZhAAAAAAAAk8bpk8bpk8bpk8bpjsHlAAAAwMDA//////// AAAA/wAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAAAD/AAAA////wMDA AAAAk8bplcjrk8bpk8bpk8bpk8bpAAAAHjZhACNGAAAAAAAAAAAASIT/SIT/AAAAAP//AP//AP// AAAAAAAAAP//AAAASIT/SIT/AAAAAAAAAAAAACNGHjZhAAAAk8bpk8bpk8bpk8bpk8bpjsHlAAAA wMDA////////AAAA/wAAAAAAAAAAAAAAAAAAk8bpk8bpk8bpk8bpk8bpAAAAAAAAAAAAAAAAAAD/ AAAA////wMDAAAAAk8bplcjrlcjricPqAAAAAAAAAAAAAAAAACNGAAAAgIAAgIAAAAAAFhsmAAAA AP//AP//AP//AAAAAAAAAP//AAAAFhsmAAAAgIAAgIAAAAAAACNGAAAAAAAAAAAAAAAAicPqk8bp k8bpk8XnAAAAwMDAwMDA////AAAA/wAA/wAA/wAA/wAAAAAApMvmk8bpk8bpk8bpk8bpAAAAAAD/ AAD/AAD/AAD/AAAAwMDAwMDAAAAAk8bpk8XnlcjrAAAAHjZhHjZhHjZhAAAAAAAAAAAAgIAAgIAA gIAAgIAAgIAAAAAAAP//AP//AAAAAAAAAAAAgIAAgIAAgIAAgIAAgIAAAAAAAAAAAAAAHjZhHjZh HjZhAAAAAAAAlcjrlcjrk8bpAAAAwMDA////////AAAA/wAA/wAA/wAAAAAAk8bpk8bpk8bpk8bp k8bpAAAAAAD/AAD/AAD/AAAA////wMDAAAAAk8bplcjrlcjrAAAAHjZhAAAAAAAAAAAAgIAAgIAA AAAAgIAA//8A//8A//8AgIAAgIAAAAAAAP//AAAAAAAAgIAAgIAA//8A//8A//8AgIAAAAAAgIAA gIAAAAAAAAAAAAAAHjZhHjZhAAAAlcjrk8bpAAAAwMDAwMDA////////AAAAAAAAAAAAk8bpk8bp AAAAk8bpAAAAk8bpAAAAAAAAAAAAAAAA////wMDAwMDAAAAAk8bplcjrAAAAHjZhAAAAgIAAgIAA gIAAgIAAgIAAAAAAgIAA//8A//8A//8A//8AgIAAgIAAAAAAAAAAAAAAgIAA//8A//8A//8A//8A gIAAAAAAgIAAgIAAgIAAgIAAgIAAAAAAAAAAHjZhAAAAlcjrk8bpAAAAwMDAwMDAwMDAwMDAAAAA k8bplcjrAAAAAAAAk8bpAAAAAAAAk8bpAAAAwMDAwMDAwMDAwMDAAAAAk8bpk8bpAAAAHjZhAAAA gIAAgIAA//8A//8AgIAAAAAAgIAAgIAA//8A//8A//8A//8A//8AgIAAgIAAAAAAAAAA//8A//8A //8A//8A//8AgIAAgIAAAAAAgIAA//8A//8AgIAAgIAAgIAAAAAAHjZhAAAAAAAAAAAAAAAAAAAA AAAAAAAAk8bpk8bpAAAAAAAAk8bpk8bpAAAAHjZhAAAAk8bpAAAAAAAAAAAAAAAAk8bplcjrAAAA HjZhAAAAgIAAgIAA//8A//8AgIAAgIAAAAAAgIAA//8A//8A//8A//8A//8A//8A//8AgIAAAAAA AAAA//8A//8A//8A//8A//8A//8AgIAAAAAAgIAAgIAA//8A//8AgIAAgIAAgIAAAAAAHjZhHjZh AAAAk8bpk8bpk8bpk8bplcjrAAAAAAAAk8bpk8bpk8bpk8XnAAAAHjZhAAAAk8bpk8bpk8bpk8bp k8bpAAAAHjZhAAAAgIAAgIAA//8A//8AgIAAgIAAAAAAAAAAgIAA//8A//8A//8A//8A//8A//8A //8A//8AAAAAAAAA//8A//8A//8A//8A//8A//8AgIAAAAAAAAAAgIAAgIAA//8A//8A//8AgIAA gIAAAAAAAAAAHjZhAAAAk8bpk8bpk8bpAAAAHjZhAAAAk8Xnlcjrk8bpk8bpAAAAHjZhHjZhAAAA k8bpk8bpk8bpAAAAHjZhAAAAgIAAgIAA//8A//8AgIAAgIAAAAAAgIAAgIAAgIAA//8A//8A//8A //8A//8A//8A//8A//8AAAAAAAAA//8A//8A//8A//8A//8A//8AgIAAgIAAgIAAAAAAgIAAgIAA //8A//8A//8AgIAAgIAAgIAAAAAAHjZhAAAAAAAAAAAAHjZhAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAHjZhHjZhAAAAAAAAAAAAHjZhAAAAgIAAgIAA//8AgIAAgIAAgIAAAAAAgIAAgIAA//8A//8A //8A//8A//8A//8A//8A//8A//8A//8AAAAAAAAA//8A//8A//8A//8A//8A//8A//8A//8AgIAA gIAAAAAAgIAAgIAAgIAA//8A//8A//8AgIAAgIAAAAAAAAAAAAAAHjZhHjZhAAAAHjZhHjZhHjZh HjZhHjZhHjZhAAAAHjZhAAAAHjZhHjZhAAAAAAAAgIAAgIAA//8AgIAAgIAAAAAAAAAAgIAAgIAA //8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAAAA//8A//8A//8A//8A//8A//8A //8A//8A//8AgIAAgIAAAAAAAAAAgIAAgIAAgIAA//8A//8AgIAAgIAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAgIAAgIAA//8AgIAAgIAAAAAAgIAA gIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAAAAAP//AP//AAAA gIAAgIAA//8A//8A//8A//8A//8A//8AgIAAgIAAAAAAAAAAgIAAgIAAgIAA//8AgIAAgIAAgIAA gIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAA//8AgIAAgIAA AAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAAAA AP//AP//AAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8AgIAAgIAAgIAAAAAAAAAAgIAAgIAA //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAA gIAAgIAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A gIAAAAAAAAAAAP//AAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAgIAA gIAAAAAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAAgIAA gIAAgIAAgIAAAAAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8AgIAAAAAAAAAAAP//AAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8AgIAAgIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8AgIAAAAAAAAAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8AgIAAgIAAgIAAgIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAgIAAgIAAgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAAAAAgIAAgIAA//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDAwMDAwMDAwMDAwMDA wMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAAAAAgIAA//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDAwMDA//// ////////gIAAgIAAgIAAgIAAgIAAgIAA////////wMDAwMDAAAAA//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAAAAAgIAA //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAA wMDA////////////////////gIAA////////////////////////////wMDAwMDAAAAA//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A AAAAAAAA//8A//8A//8A//8A//8AgIAAgIAAgIAAgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A //8A//8AAAAAwMDA//////////////////8A//8AgIAAgIAAgIAA////////////////////wMDA AAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAgIAAgIAAgIAA//8A //8A//8A//8AAAAAAAAAgIAAgIAAgIAAgIAAgIAAgIAAAAAAAAAAAAAAAAAAgIAA//8A//8A//8A //8A//8A//8A//8A//8AAAAAwMDAwMDA//////////////////////////8A//////////////// ////wMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAA AAAAgIAAgIAAgIAAgIAA//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAP//AP//AP//AAAAgIAA //8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDAwMDA//////////////////////////8A ////////////wMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A gIAAgIAAAAAAAP//AAAAAAAAAAAAgIAAgIAAAAAAAAAAAP//AP//AP//AP//AP//AP//AP//AP// AAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDAwMDA//////8A//8A //8A//8A//8A//8A////wMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8AgIAAAAAAAP//AP//AP//AP//AP//AAAAAAAAAAAAAAAAAP//AP//AP//AP//AP// AP//AP//AP//AAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AAAAAwMDA wMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAAAAA//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP// AP//AP//AP//AP//AP//AP//AAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAA AAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAgIAAAAAAAP//AP//AP//AP//AP// AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAA//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8AgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAP//AP//AP// AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAA//8A//8A//8A //8A//8A//8A//8AgIAAgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8AgIAAgIAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8AgIAAAAAA AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAA //8A//8A//8A//8A//8AgIAAgIAAgIAAAAAAAAAAgIAA//8A//8A//8A//8A//8A//8A//8A//8A //8A//8A//8A//8A//8A//8A//8A//8A//8AgIAAAAAAAP//AAAAgIAAgIAA//8A//8A//8A//8A //8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP//AP//AP//AP// AAAAgIAAgIAA//8A//8AgIAAgIAAgIAAgIAAAAAAAAAAAP//AAAAgIAA//8A//8A//8A//8A//8A //8AgIAAgIAAgIAA//8A//8A//8A//8A//8A//8A//8AgIAAgIAAAAAAAP//AP//AAAAgIAAgIAA //8A//8A//8AgIAAgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAAAP//AP//AP// AP//AP//AP//AAAAgIAAgIAAgIAAgIAAgIAAAAAAAAAAAAAAAP//AP//AP//AAAAgIAA//8A//8A //8A//8A//8AgIAAgIAAAAAAgIAAgIAA//8A//8A//8A//8A//8A//8AgIAAAAAAAP//AP//AP// AP//AAAAgIAAgIAA//8A//8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AP//AP//AAAAAAAA AP//AP//AP//AP//AP//AP//AAAAgIAAgIAAAAAAAAAAAAAAAP//AP//AP//AP//AP//AP//AAAA gIAA//8A//8A//8A//8AgIAAgIAAAAAAAP//AAAAgIAA//8A//8A//8A//8A//8AgIAAgIAAAAAA AP//AP//AP//AP//AP//AAAAgIAAgIAA//8AgIAAAAAAAP//AP//AP//AP//AP//AP//AP//AP// AP//AAAAAAAAAP//AP//AP//AP//AP//AAAAgIAAAAAAAAAAAP//AP//AP//AP//AP//AP//AP// AP//AP//AAAAgIAA//8A//8A//8AgIAAgIAAAAAAAP//AP//AAAAgIAAgIAA//8A//8A//8A//8A gIAAAAAAAP//AP//AP//AP//AP//AP//AP//AAAAgIAAgIAAgIAAAAAAAP//AP//AP//AP//AP// AP//AP//AP//AP//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
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

    public boolean addParticipant(User participant) {
        if (participant == null) {
            throw new NullPointerException("participant cannot be null");
        }
        if (mParticipants.size() < mNumberMaxOfParticipants) {
            mParticipants.add(participant);
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

    public boolean removeParticipant(User participant) {
        if (participant == null) {
            throw new NullPointerException("participant cannot be null");
        }
        Log.d("Event.addParticipant", "removing the participant");
        return mParticipants.remove(participant);
    }

    public boolean checkIfParticipantIsIn(User participant) {
        return mParticipants.contains(participant);
    }

    public static Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public boolean isFull() {
        return mParticipants.size() >= mNumberMaxOfParticipants;
    }

    public String getProperDateString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.FRANCE);
        timeFormat.setTimeZone(TimeZone.getTimeZone("Europe/Zurich"));
        return timeFormat.format(mStartDate.getTime());
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

    public int getCreator() {
        return mCreatorId;
    }

    public String getTagsString() {
        if (mTags.contains("Foot!") ||
                mTags.contains("Football")) {
            return "Football";
        } else if (mTags.contains("Basketball")) {
            return "Basketball";
        } else {
            return "Unknown";
        }
    }

    public void setListOfParticipant(Set<User> str) {
        mParticipants = str;
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

        return stringToBitMap(mPicture);
    }

    public void setPicture(Bitmap bitmap) {
        if (bitmap != null) {
            mPicture = bitmapToString(bitmap);
        } else {
            mPicture = "";
        }

    }

    @Override
    public LatLng getPosition() {
        return mLocation;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Event) {
            Event event = (Event) object;
            return mID == event.mID;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return mID;
    }


}


