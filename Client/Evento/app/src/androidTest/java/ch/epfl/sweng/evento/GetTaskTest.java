package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


import static junit.framework.Assert.assertEquals;

/**
 * Created by joachimmuth on 21.10.15.
 * Test the REST API
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GetTaskTest {
    private GetTask getTask;
    private static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final int ASCII_SPACE = 0x20;
    private HttpURLConnection connection;
    private NetworkProvider networkProvider;
    private static final String wrongUrl = "wrongurl";


    @Before
    public void setUp() throws Exception {
        connection = Mockito.mock(HttpURLConnection.class);
        networkProvider = Mockito.mock(NetworkProvider.class);
        Mockito.doReturn(connection).when(networkProvider).getConnection(Mockito.any(URL.class));
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

        getTask = new GetTask("http://example.com", networkProvider,
                new RestTaskCallback (){
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
}
