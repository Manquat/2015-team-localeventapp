package ch.epfl.sweng.evento.gui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ch.epfl.sweng.evento.R;

public class ManageActivity extends AppCompatActivity {

    private TextView mMatchedEvent;
    private TextView mHostedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);


    }

}
