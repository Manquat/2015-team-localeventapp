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
import java.util.Vector;
import java.util.regex.Pattern;

import ch.epfl.sweng.evento.EventActivity;
import ch.epfl.sweng.evento.R;
import ch.epfl.sweng.evento.tabsFragment.MyView.MyView;

/**
 * Simple Fragment used to display some meaningful content for each page in the sample's
 * {@link android.support.v4.view.ViewPager}.
 */
public class ContentFragment extends Fragment implements MyView.OnToggledListener {

    final int PADDING = 5;
    private static Vector<ImageButton> m_mosaicVector = new Vector<ImageButton>();

    private GridLayout m_gridLayout;
    private Activity mActivity;
    private int m_numberOfRow;
    private int m_numberOfColumn;
    private MyView[] m_myViews;

    /**
     * @return a new instance of {@link ContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public ContentFragment(){
        super();
        m_numberOfColumn = 3;
        m_numberOfRow = 3;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void OnToggled(MyView v, boolean touchOn) {
        Intent intent = new Intent(mActivity, EventActivity.class);
        mActivity.startActivity(intent);

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

        m_gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        m_gridLayout.setRowCount(m_numberOfRow);
        m_gridLayout.setColumnCount(m_numberOfColumn);

        m_myViews = new MyView[m_numberOfRow*m_numberOfColumn];

        for(int yPos=0; yPos<m_numberOfRow; yPos++){
            for(int xPos=0; xPos<m_numberOfColumn; xPos++){
                MyView tView = new MyView(view.getContext(), xPos, yPos);
                tView.setOnToggledListener(this);
                m_myViews[yPos*m_numberOfColumn + xPos] = tView;
                m_gridLayout.addView(tView);
            }
        }

        //Rescale
        m_gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener(){

                    @Override
                    public void onGlobalLayout() {
                        int pWidth = m_gridLayout.getWidth();
                        int pHeight = m_gridLayout.getHeight();
                        int widthColumn = pWidth/m_numberOfColumn;
                        int heightRow = pHeight/m_numberOfRow;

                        for(int yPos=0; yPos<m_numberOfRow; yPos++){
                            for(int xPos=0; xPos<m_numberOfColumn; xPos++){
                                GridLayout.LayoutParams params =
                                        (GridLayout.LayoutParams)m_myViews[yPos*m_numberOfColumn + xPos].getLayoutParams();
                                params.width = widthColumn - 2*PADDING;
                                params.height = heightRow - 2*PADDING;
                                params.setMargins(PADDING, PADDING, PADDING, PADDING);
                                m_myViews[yPos*m_numberOfColumn + xPos].setLayoutParams(params);
                            }
                        }

                    }});

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
