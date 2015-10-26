package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ch.epfl.sweng.evento.RestApi.RestApi;

import static junit.framework.Assert.assertEquals;

/**
 * Created by joachimmuth on 23.10.15.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RestApiServerTest {
    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();
    private static final String urlServer = "http://10.0.2.2:8000/";


    @Test
    public void testGetEvent() {
        RestApi restApi = new RestApi(networkProvider, urlServer);
        ArrayList<Event> eventArrayList = new ArrayList<Event>();
        assertEquals("Before requesting, eventArrayList is empty", eventArrayList.size(), 0);

        restApi.getEvent(eventArrayList);

        restApi.waitUntilFinish();

        assertEquals("We get one event after requesting once", eventArrayList.size(), 1);

    }

}
