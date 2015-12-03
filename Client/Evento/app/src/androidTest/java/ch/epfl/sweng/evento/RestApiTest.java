package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.Parser;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.Serializer;
import ch.epfl.sweng.evento.rest_api.callback.GetEventCallback;
import ch.epfl.sweng.evento.rest_api.callback.HttpResponseCodeCallback;
import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;
import ch.epfl.sweng.evento.rest_api.task.GetTask;
import ch.epfl.sweng.evento.rest_api.task.PostTask;
import ch.epfl.sweng.evento.rest_api.task.PutTask;

import static junit.framework.Assert.assertEquals;

/**
 * Created by joachimmuth on 21.10.15.
 * Test the REST API
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RestApiTest {
    private static final String TAG = "RestApiTest";
    private GetTask getTask;
    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final int ASCII_SPACE = 0x20;
    private HttpURLConnection connection;
    private NetworkProvider networkProviderMockito;
    private static final String wrongUrl = "http://example.com";
    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    //private static final String urlServer = "http://10.0.2.2:8000/";
    private static final String urlServer = "https://protected-hamlet-4797.herokuapp.com/";

    private static final Parser parser = new Parser();
    private static final String PROPER_JSON_STRING = "{\n"
            + "  \"id\": 17005,\n"
            + "  \"Event_name\": \"My football game\",\n"
            + "  \"tags\": \"" + "Foot" + "\",\n"
            + "  \"image\": \n"
            + "    \"" + "image" + "\" ,\n"
            + "  \"description\": \n"
            + "    \"Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!\" ,\n"
            + "  \"latitude\": 46.519428,\n"
            + "  \"longitude\": 6.580847,\n"
            + "  \"date\": \"26/02/1992 at 13:30\",\n"
            + "  \"address\": \"Terrain de football de Dorigny\", \n "
            + "  \"creator\": \"Micheal Jackson\"\n"
            + "}\n";
    private static final Event PROPER_EVENT = new Event(
            17005,
            "My football game",
            "Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!",
            46.519428, 6.580847,
            "Terrain de football de Dorigny",
            "Micheal Jackson",
            new HashSet<String>(),
            "image",
            new HashSet<User>());


    @Before
    public void setUp() throws Exception {
        connection = Mockito.mock(HttpURLConnection.class);
        networkProviderMockito = Mockito.mock(NetworkProvider.class);
        Mockito.doReturn(connection).when(networkProviderMockito).getConnection(Mockito.any(URL.class));
    }

    private void configureResponse(int status, String content, String contentType)
            throws IOException {
        InputStream dataStream = new ByteArrayInputStream(content.getBytes());
        Mockito.doReturn(status).when(connection).getResponseCode();
        Mockito.doReturn(dataStream).when(connection).getInputStream();
        Mockito.doReturn(contentType).when(connection).getContentType();
    }

    private void configureCrash(int status) throws IOException {
        InputStream dataStream = Mockito.mock(InputStream.class);
        Mockito.when(dataStream.read())
                .thenReturn(ASCII_SPACE, ASCII_SPACE, ASCII_SPACE, ASCII_SPACE)
                .thenThrow(new IOException());

        Mockito.doReturn(status).when(connection).getResponseCode();
        Mockito.doReturn(dataStream).when(connection).getInputStream();
    }

    /**
     * @throws JSONException
     */
    @Test
    public void testParsingJsonToEvent() throws JSONException {
        JSONObject jsonObject = new JSONObject(PROPER_JSON_STRING);
        Event eventFromJson = Parser.parseFromJSON(jsonObject);

        //assertEquals("Event correctly parsed", eventFromJson, PROPER_EVENT);

        assertEquals("id correctly parsed", PROPER_EVENT.getID(), eventFromJson.getID());
        assertEquals("title correctly parsed", PROPER_EVENT.getTitle(), eventFromJson.getTitle());
        assertEquals("description correctly parsed", PROPER_EVENT.getDescription(), eventFromJson.getDescription());
        assertEquals("xLoc correctly parsed", PROPER_EVENT.getLatitude(), eventFromJson.getLatitude());
        assertEquals("yLoc correctly parsed", PROPER_EVENT.getLongitude(), eventFromJson.getLongitude());
        assertEquals("address correctly parsed", PROPER_EVENT.getAddress(), eventFromJson.getAddress());
        assertEquals("creator correctly parsed", PROPER_EVENT.getCreator(), eventFromJson.getCreator());

    }

    @Test
    public void testSerializer() throws JSONException {
        String event_string = Serializer.event(PROPER_EVENT);
        JSONObject event_json = new JSONObject(event_string);
        assertEquals("Event title correctly serialized to string", event_json.getString("Event_name"), PROPER_EVENT.getTitle());
        assertEquals("Event latitude correctly serialized to string", event_json.getDouble("latitude"), PROPER_EVENT.getLatitude());
    }

    /**
     * Test if GetTask works with a simple mockito string response
     *
     * @throws IOException
     */
    @Test
    public void testGetTaskLocal() throws IOException, ExecutionException, InterruptedException {
        final String testString = "test string";
        configureResponse(HttpURLConnection.HTTP_OK, testString, JSON_CONTENT_TYPE);

        getTask = new GetTask(wrongUrl, networkProviderMockito,
                new RestTaskCallback() {
                    public void onTaskComplete(String response) {
                        assertEquals(testString + "\n", response);
                    }
                });

        getTask.execute().get();

    }


    @Test
    public void testGetEventLocal() throws IOException, InterruptedException {
        configureResponse(HttpURLConnection.HTTP_OK, PROPER_JSON_STRING, JSON_CONTENT_TYPE);
        RestApi restApi = new RestApi(networkProviderMockito, wrongUrl);
        final ArrayList<Event> eventArrayList = new ArrayList<Event>();
        restApi.getEvent(new GetEventCallback() {
            @Override
            public void onEventReceived(Event event) {
                eventArrayList.add(event);
            }
        });

        Thread.sleep(500);


        //assertNotNull("Event is not null", eventArrayList);
        assertEquals("We get one event after requesting once", eventArrayList.size(), 1);
        assertEquals("id", eventArrayList.get(0).getID(), PROPER_EVENT.getID());
        assertEquals("title", eventArrayList.get(0).getTitle(), PROPER_EVENT.getTitle());
        assertEquals("description", eventArrayList.get(0).getDescription(), PROPER_EVENT.getDescription());

    }
    @Ignore("If server modification have been done the test fails")
    @Test
    public void testGetEventServer() throws InterruptedException {
        RestApi restApi = new RestApi(networkProvider, urlServer);
        final ArrayList<Event> eventArrayList = new ArrayList<>();
        assertEquals("Before requesting, eventArrayList is empty", eventArrayList.size(), 0);

        restApi.getEvent(new GetEventCallback() {
            @Override
            public void onEventReceived(Event event) {
                if (event != null) {
                    Log.d(TAG, event.getTitle());
                    Log.d(TAG, Integer.toString((event.getID())));
                    eventArrayList.add(event);
                }
            }
        });

        Thread.sleep(2000);

        assertEquals("We get one event after requesting once", 1, eventArrayList.size());

        restApi.getEvent(new GetEventCallback() {
            @Override
            public void onEventReceived(Event event) {
                eventArrayList.add(event);
            }
        });

        Thread.sleep(200);


        assertEquals("We get two event after requesting twice", eventArrayList.size(), 2);


    }

    private static final String EVENT_TO_CREATE = "{\n"
            + "  \"Event_name\": \"Ping-Pong at Sat 2\",\n"
            + "  \"description\": \n"
            + "    \"Beer, ping-pong... let's beerpong\" ,\n"
            + "  \"latitude\": 46.519428,\n"
            + "  \"longitude\": 6.580847,\n"
            + "  \"address\": \"Satellite\", \n"
            + "  \"date\" : \"1991-01-15T23:00:00Z\",\n "
            + "   \"creator\": \"Guillaume Meyrat\"\n"
            + "}\n";


    private static final Calendar date = new GregorianCalendar(1990, 12, 16, 0, 0);
    private static final Event e = new Event(10, "Ping-Pong at Sat 2", "Beer, ping-pong... let's beerpong",
            46.519428, 6.580847, "Satellite", "Guillaume Meyrat", new HashSet<String>(), date, date);
    private static final String EVENT_TO_CREATE_seri = Serializer.event(e);


    @Test
    public void testPostTaskServer() throws ExecutionException, InterruptedException {
        String url = urlServer + "events/";
        PostTask postTask = new PostTask(url, networkProvider, EVENT_TO_CREATE, new RestTaskCallback() {
            public void onTaskComplete(String response) {
            }
        });

        postTask.execute().get();
    }

    @Test
    public void testPostEvent() {
        RestApi restApi = new RestApi(networkProvider, urlServer);

        restApi.postEvent(e, new HttpResponseCodeCallback() {
            @Override
            public void onSuccess(String response) {
                // nothing
            }
        });
    }


    @Test
    public void testPutTask() throws ExecutionException, InterruptedException {
        String EVENT_TO_PUT = "{\n"
                + "  \"Event_name\": \"Ping-Pong at Sat 2\",\n"
                + "  \"description\": \n"
                + "    \"This event was send using PUT method!\" ,\n"
                + "  \"latitude\": 46.519428,\n"
                + "  \"longitude\": 6.580847,\n"
                + "  \"address\": \"Satellite\", \n "
                + "  \"creator\": \"Guillaume Meyrat\"\n"
                + "}\n";

        String url = urlServer + "events/11";
        final ArrayList<Event> eventArrayList = new ArrayList<>();

        // put event id = 11;
        PutTask putTask = new PutTask(url, networkProvider, EVENT_TO_PUT, new RestTaskCallback() {
            @Override
            public void onTaskComplete(String result) {
                // nothing
            }
        });

        putTask.execute().get();
    }

    @Test
    public void testUpdateEvent() throws InterruptedException {
        Event event = new Event(14, "this is a test of UpdateEvent", "test1", 0, 0, "address", "creator", new HashSet<String>(), "image", new HashSet<User>());
        RestApi restApi = new RestApi(networkProvider, urlServer);
        restApi.updateEvent(event, new HttpResponseCodeCallback() {
            @Override
            public void onSuccess(String response) {

            }
        });

        Thread.sleep(500);

    }

    @Test
    public void testDeleteEvent() throws InterruptedException {
        RestApi restApi = new RestApi(networkProvider, urlServer);
        restApi.deleteEvent(15, new HttpResponseCodeCallback() {
            @Override
            public void onSuccess(String response) {

            }
        });

        Thread.sleep(500);

    }
}
