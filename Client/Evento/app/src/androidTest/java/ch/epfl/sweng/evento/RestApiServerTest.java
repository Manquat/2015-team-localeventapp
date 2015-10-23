package ch.epfl.sweng.evento;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.evento.RestApi.RestApi;

/**
 * Created by joachimmuth on 23.10.15.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RestApiServerTest {
    private static final NetworkProvider networkProvider = new DefaultNetworkProvider();



    @Test
    public void testGetEvent() {
        RestApi restApi = new RestApi(networkProvider);

    }

}
