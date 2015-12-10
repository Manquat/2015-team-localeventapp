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

package ch.epfl.sweng.evento.tabs_fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import ch.epfl.sweng.evento.EventDatabase;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.Settings;
import ch.epfl.sweng.evento.event.Event;
import ch.epfl.sweng.evento.gui.event_activity.EventActivity;
import ch.epfl.sweng.evento.rest_api.RestApi;
import ch.epfl.sweng.evento.rest_api.network_provider.DefaultNetworkProvider;
import ch.epfl.sweng.evento.tabs_fragment.MyView.MyView;

/**
 * Simple Fragment used to display some meaningful content for each page in the sample's
 * {@link android.support.v4.view.ViewPager}.
 */
public class ContentFragment extends Fragment implements Refreshable
{


    private static final int MAX_NUMBER_OF_EVENT = 50;
    private static final String TAG = "ContentFragment";
    private static Vector<ImageButton> mMosaicVector = new Vector<ImageButton>();
    final int PADDING = 5;
    public Calendar dateFilter;
    private int mNumberOfEvent;
    private List<Event> mEvents;
    private RestApi mRestAPI = new RestApi(new DefaultNetworkProvider(), Settings.getServerUrl());
    private GridLayout mGridLayout;
    private Activity mActivity;
    private int mNumberOfRow;
    private int mNumberOfColumn;
    private int mWidthColumn;
    private int mHeightRow;
    private Vector<boolean[]> mDisplayOrNot;
    private Vector<MyView> mMyViews;
    private View mView;
    private Toolbar mToolbar;


    /**
     * Create a new instance of {@link ContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public ContentFragment()
    {
        super();
        mNumberOfColumn = 3;
        mNumberOfRow = 4;
        mDisplayOrNot = new Vector<boolean[]>();
        for (int i = 0; i < 2 * MAX_NUMBER_OF_EVENT / mNumberOfColumn + 1; ++i)
        {
            boolean[] tmpBooleanRow = new boolean[mNumberOfColumn];
            for (int j = 0; j < mNumberOfColumn; ++j)
            {
                tmpBooleanRow[j] = true;
            }
            mDisplayOrNot.add(tmpBooleanRow);
        }
        mMyViews = new Vector<MyView>();
        mEvents = new ArrayList<Event>();
        mNumberOfEvent = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mView != null) refresh();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        //adding content fragment as observer of EventDatabase
        EventDatabase.INSTANCE.addObserver(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //remove content fragment as observer of EventDatabase
        EventDatabase.INSTANCE.removeObserver(this);
    }

    public void refresh()
    {

        mEvents = EventDatabase.INSTANCE.getAllEvents();
        mNumberOfEvent = mEvents.size();
        displayMosaic();
        Log.d("LOG_ContentFragment", "Refreshing");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_mosaic, container, false);

        refresh();

        return mView;
    }

    private void displayMosaic()
    {
        mGridLayout = (GridLayout) mView.findViewById(R.id.gridLayout);
        mGridLayout.setRowCount(mNumberOfRow);
        mGridLayout.setColumnCount(mNumberOfColumn);
        mGridLayout.removeAllViews();

        boolean[] tmpBooleanRow = new boolean[mNumberOfColumn];
        Span tmpSpanSmtgOrNot = Span.NOTHING;
        int spanning = 0;
        for (int yPos = 0, countEvent = 0; countEvent < MAX_NUMBER_OF_EVENT && countEvent < mNumberOfEvent; yPos++)
        {
            for (int xPos = 0; xPos < mNumberOfColumn && countEvent < MAX_NUMBER_OF_EVENT && countEvent < mNumberOfEvent; xPos++, countEvent++)
            {
                final MyView tView = new MyView(mView.getContext(), xPos, yPos);
                final int count = countEvent;
                tView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(mActivity, EventActivity.class);
                        intent.putExtra(EventActivity.CURRENT_EVENT_KEY, mEvents.get(count).getID());
                        mActivity.startActivity(intent);
                    }
                });
                if (mDisplayOrNot.get(yPos)[xPos])
                {
                    //to draw the Event's own picture instead of one based on its tags
                    tView.setImageBitmap(mEvents.get(countEvent).getPicture());

                    tView.setAdjustViewBounds(true);
                    mMyViews.add(tView);

                    switch (tmpSpanSmtgOrNot)
                    {
                        case NOTHING:
                            while (yPos >= mNumberOfRow)
                            {
                                ++mNumberOfRow;
                                mGridLayout.setRowCount(mNumberOfRow);
                            }
                            addViewToGridLayout(tView, yPos, xPos, 1, 1);
                            break;
                        case TWO_ROWS:
                            while ((yPos + 1) >= mNumberOfRow)
                            {
                                ++mNumberOfRow;
                                mGridLayout.setRowCount(mNumberOfRow);
                            }
                            addViewToGridLayout(tView, yPos, xPos, 2, 1);
                            break;
                        case TWO_COLUMNS:
                            addViewToGridLayout(tView, yPos, xPos, 1, 2);
                            break;
                    }
                } else
                {
                    countEvent--;
                }
            }
        }
    }

    private void addViewToGridLayout(View view, int row, int column, int rowSpan, int columnSpan)
    {
        int pWidth = mGridLayout.getWidth();
        int pHeight = mGridLayout.getHeight();
        mWidthColumn = 0;
        mHeightRow = 0;
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        Point size = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        int halfScreenWidth = (int) (screenWidth * 0.5);
        int quarterScreenWidth = (int) (halfScreenWidth * 0.5);
        params.width = screenWidth / 3 - 2 * PADDING;
        //params.width = mWidthColumn - 2 * PADDING;
        //params.height = mHeightRow - 2 * PADDING;
        params.setMargins(PADDING, PADDING, PADDING, PADDING);
        params.columnSpec = GridLayout.spec(column, columnSpan);
        params.rowSpec = GridLayout.spec(row, rowSpan);

        mGridLayout.addView(view, params);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    public enum Span
    {
        NOTHING, TWO_ROWS, TWO_COLUMNS
    }

}
