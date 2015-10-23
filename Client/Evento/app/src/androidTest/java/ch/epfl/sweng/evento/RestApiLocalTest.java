package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

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
import java.util.concurrent.ExecutionException;


import ch.epfl.sweng.evento.RestApi.Parser;
import ch.epfl.sweng.evento.RestApi.RestApi;
import ch.epfl.sweng.evento.RestApi.RestTaskCallback;
import ch.epfl.sweng.evento.RestApi.GetTask;

import static junit.framework.Assert.assertEquals;

/**
 * Created by joachimmuth on 21.10.15.
 * Test the REST API
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RestApiLocalTest {
    private GetTask getTask;
    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final int ASCII_SPACE = 0x20;
    private HttpURLConnection connection;
    private NetworkProvider networkProviderMockito;
    private static final String wrongUrl = "wrongurl";

    private static final Parser parser = new Parser();
    private static final String PROPER_JSON_STRING = "{\n"
            + "  \"id\": 17005,\n"
            + "  \"title\": \"My football game\",\n"
            + "  \"description\": \n"
            + "    \"Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!\" ,\n"
            + "  \"xLocation\": 46.519428,\n"
            + "  \"yLocation\": 6.580847,\n"
            + "  \"address\": \"Terrain de football de Dorigny\",\n "
            + "  \"creator\": \"Micheal Jackson\"\n"
            + "}\n";
    private static final Event PROPER_EVENT_RESULT = new Event(17005,
            "My football game",
            "Okay guys, let's play a little game this evening at dorigny. Remember: no doping allowed!",
            46.519428, 6.580847,
            "Terrain de football de Dorigny",
            "Micheal Jackson");


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
     * Test if GetTask works with a simple mockito string response
     * @throws IOException
     */
    @Test
    public void testGetTask() throws IOException {
        final String testString = "test string";
        configureResponse(HttpURLConnection.HTTP_OK, testString, JSON_CONTENT_TYPE);

        getTask = new GetTask("http://example.com", networkProviderMockito,
                new RestTaskCallback(){
                    public void onTaskComplete(String response){
                        assertEquals(testString + "\n", response);
                    }});

        try {
            getTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetEvent() throws IOException {
        configureResponse(HttpURLConnection.HTTP_OK, PROPER_JSON_STRING, JSON_CONTENT_TYPE);
        RestApi restApi = new RestApi(networkProviderMockito);
        ArrayList<Event> eventArrayList = new ArrayList<Event>();

        restApi.getEvent(eventArrayList);

        // TODO: a best way to test the end of the asyctask called by RestApi
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        assertEquals("We get one event after requesting once", eventArrayList.size(), 1);
        assertEquals("id", eventArrayList.get(0).ID(), PROPER_EVENT_RESULT.ID());
        assertEquals("title", eventArrayList.get(0).Title(), PROPER_EVENT_RESULT.Title());
        assertEquals("description", eventArrayList.get(0).Description(), PROPER_EVENT_RESULT.Description());

    }


}
