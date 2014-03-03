/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.listeners.FlipForwardButtonListener;
import com.felicekarl.foragingtech.listeners.UpdateFlipForwardButtonListener;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */
public class PagerSlidePageFragment extends Fragment implements OnClickListener, UpdateFlipForwardButtonListener{
	private static final String TAG = PagerSlidePageFragment.class.getSimpleName();
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    
    private Button btnPlay;
    private FlipForwardButtonListener mFlipForwardButtonListener;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static PagerSlidePageFragment create(int pageNumber) {
        PagerSlidePageFragment fragment = new PagerSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PagerSlidePageFragment() {
    	
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	ViewGroup view = (ViewGroup) inflater
                .inflate(R.layout.fragment_pager_slide_page, container, false);
    	
        switch(mPageNumber){
    	case 0:
    		// Inflate the layout containing a title and body text.
    		view = (ViewGroup) inflater
                    .inflate(R.layout.fragment_pager_slide_page1, container, false);
            // Set the title view to show the page number.
            ((TextView) view.findViewById(R.id.pageNumber)).setText(String.valueOf(mPageNumber + 1));
    		break;
    	case 1:
    		// Inflate the layout containing a title and body text.
    		view = (ViewGroup) inflater
                    .inflate(R.layout.fragment_pager_slide_page2, container, false);
            // Set the title view to show the page number.
            ((TextView) view.findViewById(R.id.pageNumber)).setText(String.valueOf(mPageNumber + 1));
    		break;
    	case 2:
    		// Inflate the layout containing a title and body text.
    		view = (ViewGroup) inflater
                    .inflate(R.layout.fragment_pager_slide_page3, container, false);
            // Set the title view to show the page number.
            ((TextView) view.findViewById(R.id.pageNumber)).setText(String.valueOf(mPageNumber + 1));
    		break;
        }
        
        btnPlay = (Button) view.findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        
        return view;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
    

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_play:
			mFlipForwardButtonListener.flip(mPageNumber);
		}
	}
	
	@Override
	public void onDestroyView () {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy () {
		super.onDestroy();
	}

	@Override
	public void updateFlipForwardButtonListener(FlipForwardButtonListener mFlipForwardButtonListener) {
		this.mFlipForwardButtonListener = mFlipForwardButtonListener;
	}
}
