package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.listeners.FlipBackwardButtonListener;
import com.felicekarl.foragingtech.listeners.UpdateFlipBackwardButtonListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ContentNavigatingFragment extends BaseFragment implements OnClickListener, UpdateFlipBackwardButtonListener {
	private static final String TAG = ContentNavigatingFragment.class.getSimpleName();
	
	private ContentActionBarFragment mContentActionBarFragment;
	private FlipBackwardButtonListener mFlipBackwardButtonListener;
	
    public ContentNavigatingFragment() {
    	
    }
    
    public static ContentNavigatingFragment create() {
    	ContentNavigatingFragment fragment = new ContentNavigatingFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	view = (ViewGroup) inflater.inflate(R.layout.fragment_content_navigating, container, false);
    	/* add content actionbar fragment */
    	mContentActionBarFragment = ContentActionBarFragment.create();
    	getChildFragmentManager().beginTransaction().add(R.id.content_container, mContentActionBarFragment).commit();
    	mContentActionBarFragment.updateFlipBackwardButtonListener(mFlipBackwardButtonListener);
    	
    	
    	slideUpFragment();
    	
    	return view;
    }
	
	@Override
	public void onDestroy () {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		}
	}

	@Override
	protected void enableEditText() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void disableEditText() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFlipBackwardButtonListener(FlipBackwardButtonListener mFlipBackwardButtonListener) {
		this.mFlipBackwardButtonListener = mFlipBackwardButtonListener;
		if (mContentActionBarFragment != null) {
			mContentActionBarFragment.updateFlipBackwardButtonListener(mFlipBackwardButtonListener);
		}
	}
}