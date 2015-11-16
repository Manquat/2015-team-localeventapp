package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.RestApi.DeleteResponseCallback;
import ch.epfl.sweng.evento.RestApi.GetResponseCallback;
import ch.epfl.sweng.evento.RestApi.GetTask;
import ch.epfl.sweng.evento.RestApi.Parser;
import ch.epfl.sweng.evento.RestApi.PostCallback;
import ch.epfl.sweng.evento.RestApi.PostTask;
import ch.epfl.sweng.evento.RestApi.PutCallback;
import ch.epfl.sweng.evento.RestApi.PutTask;
import ch.epfl.sweng.evento.RestApi.RestApi;
import ch.epfl.sweng.evento.RestApi.RestTaskCallback;
import ch.epfl.sweng.evento.RestApi.Serializer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by joachimmuth on 21.10.15.
 * Test the REST API
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ServerTest {
    private GetTask getTask;
    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final int ASCII_SPACE = 0x20;
    private HttpURLConnection connection;
    private NetworkProvider networkProviderMockito;
    private static final String wrongUrl = "http://exemple.com";
    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    private static final String urlServer = "https://protected-hamlet-4797.herokuapp.com/";

    private static final Parser parser = new Parser();
    private static final String PROPER_JSON_STRING = "{\n"
            + "  \"id\": 17005,\n"
            + "  \"Event_name\": \"My football game\",\n"
            + "  \"description\": \n"
            + "    \"Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!\" ,\n"
            + "  \"latitude\": 46.519428,\n"
            + "  \"longitude\": 6.580847,\n"
            + "  \"address\": \"Terrain de football de Dorigny\", \n "
            + "  \"creator\": \"Micheal Jackson\"\n"
            + "}\n";
    private static final Event PROPER_EVENT = new Event(17005,
            "My football game",
            "Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!",
            46.519428, 6.580847,
            "Terrain de football de Dorigny",
            "Micheal Jackson",
            new HashSet<String>());



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

    @Test
    public void testGetEventServer() {
        RestApi restApi = new RestApi(networkProvider, urlServer);
        final ArrayList<Event> eventArrayList = new ArrayList<>();
        assertEquals("Before requesting, eventArrayList is empty", eventArrayList.size(), 0);

        restApi.getEvent(new GetResponseCallback() {
            @Override
            public void onDataReceived(Event event) {
                Log.d("TestGetEvent", event.getTitle());
                Log.d("TestGetEvent", Integer.toString((event.getID())));
                eventArrayList.add(event);
            }
        });

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("We get one event after requesting once", eventArrayList.size(), 1);

        restApi.getEvent(new GetResponseCallback() {
            @Override
            public void onDataReceived(Event event) {
                eventArrayList.add(event);
            }
        });

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("We get two event after requesting twice", eventArrayList.size(), 2);


    }



}
