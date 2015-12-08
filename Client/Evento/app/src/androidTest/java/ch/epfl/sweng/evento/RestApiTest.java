package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
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

import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.rest_api.Parser;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.Serializer;
import ch.epfl.sweng.evento.rest_api.callback.GetEventCallback;
import ch.epfl.sweng.evento.rest_api.callback.RestTaskCallback;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.rest_api.network_provider.NetworkProvider;
import ch.epfl.sweng.evento.rest_api.task.GetTask;

import static org.junit.Assert.assertNotNull;

/**
 * Created by joachimmuth on 21.10.15.
 * Test the REST API
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RestApiTest {
    private static final int MOCK_USER_ID = 1;
    private static final int ASCII_SPACE = 0x20;
    private HttpURLConnection connection;
    private NetworkProvider networkProviderMockito;
    private static final String wrongUrl = "http://mock.com";
    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    //private static final String urlServer = "http://10.0.2.2:8000/";
    private static final String urlServer = "https://protected-hamlet-4797.herokuapp.com/";

    private static final Event event = new Event(
            17005,
            "My football game",
            "Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!",
            46.519428, 6.580847,
            "Terrain de football de Dorigny",
            1,
            new HashSet<String>(),
            Event.samplePicture(),
            new HashSet<User>());


    private static final String eventStringSent = "{\n"
            //+ "  \"id\": 17005,\n"
            + "  \"Event_name\": \"My football game\",\n"
            + "  \"tags\": \"" + event.getTagsString() + "\",\n"
            + "  \"image\": \n"
            + "    \"" + event.samplePicture() + "\" ,\n"
            + "  \"description\": \n"
            + "    \"Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!\" ,\n"
            + "  \"latitude\": 46.519428,\n"
            + "  \"longitude\": 6.580847,\n"
            + "  \"address\": \"Terrain de football de Dorigny\",\n"
            + "  \"date\":\"" + event.getProperDateString() + "\",\n"
            + "  \"duration\":\"00:00:00\",\n"
            + "  \"owner\":\"" + MOCK_USER_ID + "\"\n"
            + "}\n";

    private static final String eventStringReceived = "{\n"
            + "  \"id\": 17005,\n"
            + eventStringSent.substring(2);

    @Before
    public void setUp() throws Exception {
        connection = Mockito.mock(HttpURLConnection.class);
        networkProviderMockito = Mockito.mock(NetworkProvider.class);
        Mockito.doReturn(connection).when(networkProviderMockito).getConnection(Mockito.any(URL.class));

        Settings.setUser(new User(MOCK_USER_ID, "MockJo", "mockjo@plop.ch"));
    }

    private void configureResponse(int status, String content, String contentType)
            throws IOException {
        InputStream dataStream = new ByteArrayInputStream(content.getBytes());
        Mockito.doReturn(status).when(connection).getResponseCode();
        Mockito.doReturn(dataStream).when(connection).getInputStream();
        Mockito.doReturn(contentType).when(connection).getContentType();
    }


    @Test
    public void serializerTest() throws JSONException {
        String s = Serializer.event(event);
        //JSONObject jsonObject = new JSONObject(s);
        Assert.assertEquals(eventStringSent, s);
    }

    @Test
    public void parserTest() throws JSONException {
        JSONObject jsonObject = new JSONObject(eventStringReceived);
        Event eventFromJson = Parser.toEvent(jsonObject);

        junit.framework.Assert.assertEquals("id correctly parsed", event.getID(), eventFromJson.getID());
        junit.framework.Assert.assertEquals("title correctly parsed", event.getTitle(), eventFromJson.getTitle());
        junit.framework.Assert.assertEquals("description correctly parsed", event.getDescription(), eventFromJson.getDescription());
        junit.framework.Assert.assertEquals("xLoc correctly parsed", event.getLatitude(), eventFromJson.getLatitude());
        junit.framework.Assert.assertEquals("yLoc correctly parsed", event.getLongitude(), eventFromJson.getLongitude());
        junit.framework.Assert.assertEquals("address correctly parsed", event.getAddress(), eventFromJson.getAddress());
        junit.framework.Assert.assertEquals("creator correctly parsed", event.getCreator(), eventFromJson.getCreator());
    }

    /**
     * Test if GetTask works with a simple mockito string response
     *
     * @throws IOException
     */
    @Test
    public void testGetTaskMock() throws IOException, ExecutionException, InterruptedException {
        final String testString = "test string";
        configureResponse(HttpURLConnection.HTTP_OK, testString, JSON_CONTENT_TYPE);

        GetTask getTask = new GetTask(wrongUrl, networkProviderMockito,
                new RestTaskCallback() {
                    public void onTaskComplete(String response) {
                        junit.framework.Assert.assertEquals(testString + "\n", response);
                    }
                });

        getTask.execute().get();
    }

    @Test
    public void testGetEventLocal() throws IOException, InterruptedException {
        configureResponse(HttpURLConnection.HTTP_OK, eventStringReceived, JSON_CONTENT_TYPE);
        RestApi restApi = new RestApi(networkProviderMockito, wrongUrl);
        final ArrayList<Event> eventArrayList = new ArrayList<Event>();
        restApi.getEvent(new GetEventCallback() {
            @Override
            public void onEventReceived(Event event) {
                eventArrayList.add(event);
            }
        });

        Thread.sleep(500);


        assertNotNull("Event is not null", eventArrayList);
        junit.framework.Assert.assertEquals("We get one event after requesting once", eventArrayList.size(), 1);
        junit.framework.Assert.assertEquals("id", event.getID(), eventArrayList.get(0).getID());
        junit.framework.Assert.assertEquals("title", event.getTitle(), eventArrayList.get(0).getTitle());
        junit.framework.Assert.assertEquals("description", event.getDescription(), eventArrayList.get(0).getDescription());
    }


}
