package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.evento.RestApi.GetTask;
import ch.epfl.sweng.evento.RestApi.PostTask;
import ch.epfl.sweng.evento.RestApi.RestApi;
import ch.epfl.sweng.evento.RestApi.RestException;
import ch.epfl.sweng.evento.RestApi.RestTaskCallback;
import ch.epfl.sweng.evento.Events.*;

import static junit.framework.Assert.assertEquals;

/**
 * Created by joachimmuth on 23.10.15.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RestApiServerTest {
    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    private static final String urlServer = "http://10.0.2.2:8000/";

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


    @Test
    public void testGetEvent() {
        RestApi restApi = new RestApi(networkProvider, urlServer);
        EventSet eventSet = new EventSet();
        assertEquals("Before requesting, eventArrayList is empty", eventSet.size(), 0);

        restApi.getEvent(eventSet);

        try {
            restApi.waitUntilFinish();
        } catch (RestException e) {
            e.printStackTrace();
        }

        assertEquals("We get one event after requesting once", eventSet.size(), 1);

        restApi.getEvent(eventSet);

        try {
            restApi.waitUntilFinish();
        } catch (RestException e) {
            e.printStackTrace();
        }

        assertEquals("We get two event after requesting twice", eventSet.size(), 2);


    }

    @Test
    public void testPostTask() {
        String url = urlServer + "events/";
        PostTask postTask = new PostTask(url, networkProvider, PROPER_JSON_STRING, new RestTaskCallback(){
            public void onTaskComplete(String response){
            }});

        try {
            postTask.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
