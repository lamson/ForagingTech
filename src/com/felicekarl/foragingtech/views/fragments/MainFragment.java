package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.listeners.*;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends BaseFragment implements OnClickListener, UpdateFlipForwardButtonListener {
	private static final String TAG = MainFragment.class.getSimpleName();

	private FlipForwardButtonListener mFlipForwardButtonListener;
	
	private Button btnFly;
	
	public MainFragment() {
	    
	}
	
	public static MainFragment create() {
		MainFragment fragment = new MainFragment();
        return fragment;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	view = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);
    	
    	btnFly = (Button) view.findViewById(R.id.btn_fly);
    	btnFly.setOnClickListener(this);
    	
    	return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.btn_fly:
            mFlipForwardButtonListener.flip(0);
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
	public void resetFragment() {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void updateFlipForwardButtonListener(
            FlipForwardButtonListener mFlipForwardButtonListener) {
        this.mFlipForwardButtonListener = mFlipForwardButtonListener;
    }
	
}
