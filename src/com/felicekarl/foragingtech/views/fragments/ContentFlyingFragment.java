package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.listeners.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ContentFlyingFragment extends BaseFragment implements OnClickListener, 
		UpdateFlipBackwardButtonListener, UpdateTakePhotoListener, UpdateDroneCommandListener {
	private static final String TAG = ContentFlyingFragment.class.getSimpleName();
	
	private ContentActionBarFragment mContentActionBarFragment;
	private FlipBackwardButtonListener mFlipBackwardButtonListener;
	private TakePhotoListener mTakePhotoListener;
	private DroneCommandListener mDroneCommandListener;
	
    public ContentFlyingFragment() {
    	
    }
    
    public static ContentFlyingFragment create() {
    	ContentFlyingFragment fragment = new ContentFlyingFragment();
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	view = (ViewGroup) inflater.inflate(R.layout.fragment_content_flying, container, false);
    	/* add content actionbar fragment */
    	mContentActionBarFragment = ContentActionBarFragment.create();
    	getChildFragmentManager().beginTransaction().add(R.id.content_container, mContentActionBarFragment).commit();
    	/* update listeners to actionbar fragment */
    	mContentActionBarFragment.updateFlipBackwardButtonListener(mFlipBackwardButtonListener);
    	mContentActionBarFragment.updateTakePhotoListener(mTakePhotoListener);
    	mContentActionBarFragment.updateDroneCommandListener(mDroneCommandListener);
    	
    	
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
			mContentActionBarFragment.updateFlipBackwardButtonListener(this.mFlipBackwardButtonListener);
		}
	}

	@Override
	public void updateTakePhotoListener(TakePhotoListener mTakePhotoListener) {
		this.mTakePhotoListener = mTakePhotoListener;
		if (mContentActionBarFragment != null) {
			mContentActionBarFragment.updateTakePhotoListener(this.mTakePhotoListener);
		}
	}

	@Override
	public void updateDroneCommandListener(DroneCommandListener mDroneCommandListener) {
		this.mDroneCommandListener = mDroneCommandListener;
		if (mContentActionBarFragment != null) {
			mContentActionBarFragment.updateDroneCommandListener(mDroneCommandListener);
		}
	}
}