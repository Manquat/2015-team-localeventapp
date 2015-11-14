/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.epfl.sweng.evento.tabsFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import ch.epfl.sweng.evento.DefaultNetworkProvider;
import ch.epfl.sweng.evento.EventActivity;
import ch.epfl.sweng.evento.Events.Event;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.RestApi.GetResponseCallback;
import ch.epfl.sweng.evento.RestApi.RestApi;
import ch.epfl.sweng.evento.tabsFragment.MyView.MyView;

/**
 * Simple Fragment used to display some meaningful content for each page in the sample's
 * {@link android.support.v4.view.ViewPager}.
 */
public class ContentFragment extends Fragment implements MyView.OnToggledListener {

    private final int PADDING = 5;
    private static final int NUMBER_OF_EVENT = 50;

    private static Vector<ImageButton> mMosaicVector = new Vector<ImageButton>();
    private List<Event> mEvents;
    private RestApi mRestAPI;

    private static final Event mockEventFootball = new Event(1, "Event1", "This is a first event", 1.1, 1.1,
            "1 long street", "Football", new HashSet<String>(Arrays.asList("Football")), new Event.Date(), new Event.Date());   // a mock event that would be replicated all over the map
    private static final Event mockEventBasket = new Event(1, "Event2", "This is a second event", 1.1, 1.1,
            "1 long street", "Basketball", new HashSet<String>(Arrays.asList("Basketball")), new Event.Date(), new Event.Date());   // a mock event that would be replicated all over the map

    private GridLayout mGridLayout;
    private Activity mActivity;
    private int mNumberOfRow;
    private int mNumberOfColumn;
    private Vector<boolean[]> mDisplayOrNot;
    private Vector<MyView> mMyViews;

    /**
     * @return a new instance of {@link ContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public ContentFragment() {
        super();
        mNumberOfColumn = 3;
        mNumberOfRow = 4;
        mDisplayOrNot = new Vector<boolean[]>();
        for (int i = 0; i < 2 * NUMBER_OF_EVENT / mNumberOfColumn + 1; ++i) {
            boolean[] tmpBooleanRow = new boolean[mNumberOfColumn];
            for (int j = 0; j < mNumberOfColumn; ++j) {
                tmpBooleanRow[j] = true;
            }
            mDisplayOrNot.add(tmpBooleanRow);
        }
        mMyViews = new Vector<MyView>();
    }

    public enum Span {NOTHING, TWO_ROWS, TWO_COLUMNS}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void OnToggled(MyView v, boolean touchOn) {
        //Intent intent = new Intent(mActivity, EventActivity.class);
        //mActivity.startActivity(intent);

        //This toast may be useful
        /*String idString = v.getIdX() + ":" + v.getIdY();

        Toast.makeText(mActivity,
                "Toogled:\n" +
                        idString + "\n" +
                        touchOn,
                Toast.LENGTH_SHORT).show();*/

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mosaic, container, false);

        mEvents = new ArrayList<Event>();
        mRestAPI = new RestApi(new DefaultNetworkProvider(), getString(R.string.url_server));
        for (int i = 0; i < NUMBER_OF_EVENT; i++) {
            mRestAPI.getEvent(new GetResponseCallback() {
                @Override
                public void onDataReceived(Event event) {
                    mEvents.add(event);
                }
            }); //TODO remove the cast once the change in restAPI is made
            //
        }

        Random rand = new Random();
        for (int i = 0; i < NUMBER_OF_EVENT; i++) {
            if (rand.nextInt(2) == 0) mEvents.add(mockEventBasket);
            else mEvents.add(mockEventFootball);
        }
        mGridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        mGridLayout.setRowCount(mNumberOfRow);
        mGridLayout.setColumnCount(mNumberOfColumn);


        boolean[] tmpBooleanRow = new boolean[mNumberOfColumn];
        Span tmpSpanSmtgOrNot = Span.NOTHING;
        for (int yPos = 0, countEvent = 0; countEvent < NUMBER_OF_EVENT; yPos++) {
            Log.d("yPos :", Integer.toString(yPos));
            Log.d("Event :", Integer.toString(countEvent));
            Log.d("Number of row :", Integer.toString(mNumberOfRow));

            for (int xPos = 0; xPos < mNumberOfColumn && countEvent < NUMBER_OF_EVENT; xPos++, countEvent++) {
                MyView tView = new MyView(view.getContext(), xPos, yPos);
                if (mDisplayOrNot.get(yPos)[xPos]) {
                    switch (mEvents.get(countEvent).getCreator()) {
                        case "Football":
                            tmpSpanSmtgOrNot = Span.NOTHING;
                            tView.setImageResource(R.drawable.football);
                            break;
                        case "Basketball":
                            tmpSpanSmtgOrNot = Span.TWO_ROWS;
                            tView.setImageResource(R.drawable.basket);
                            mDisplayOrNot.get(yPos + 1)[xPos] = false;
                            break;
                        default:
                            Log.d("Warning ", "ContentFragment.OnCreateView.mEvent_DoesntMAtch");
                            break;
                    }
                    tView.setOnToggledListener(this);
                    mMyViews.add(tView);

                    switch (tmpSpanSmtgOrNot) {
                        case NOTHING:
                            while (yPos >= mNumberOfRow) {
                                ++mNumberOfRow;
                                mGridLayout.setRowCount(mNumberOfRow);
                            }
                            addViewToGridLayout(tView, yPos, xPos, 1, 1);
                            break;
                        case TWO_ROWS:
                            while ((yPos + 1) >= mNumberOfRow) {
                                ++mNumberOfRow;
                                mGridLayout.setRowCount(mNumberOfRow);
                            }
                            addViewToGridLayout(tView, yPos, xPos, 2, 1);
                            break;
                        case TWO_COLUMNS:
                            addViewToGridLayout(tView, yPos, xPos, 1, 2);
                            break;
                    }
                } else {
                    countEvent--;
                }
            }
        }

        return view;
    }

    private void addViewToGridLayout(View view, int row, int column, int rowSpan, int columnSpan) {
        int pWidth = mGridLayout.getWidth();
        int pHeight = mGridLayout.getHeight();
        int widthColumn = pWidth / mNumberOfColumn;
        int heightRow = pHeight / mNumberOfRow;
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = widthColumn - 2 * PADDING;
        params.height = heightRow - 2 * PADDING;
        params.setMargins(PADDING, PADDING, PADDING, PADDING);
        params.columnSpec = GridLayout.spec(column, columnSpan);
        params.rowSpec = GridLayout.spec(row, rowSpan);

        mGridLayout.addView(view, params);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
