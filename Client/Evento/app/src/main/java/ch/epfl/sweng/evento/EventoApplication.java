package ch.epfl.sweng.evento;

import android.app.Application;

/**
 * Created by Val on 28.10.2015.
 */
public class EventoApplication extends Application {


    public EventoApplication() {
        // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    EventDatabase getEventDatabase() {
        return EventDatabase.INSTANCE;
    }
}
