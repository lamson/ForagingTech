package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashFragment extends BaseFragment{
	@SuppressWarnings("unused")
	private static final String TAG = SplashFragment.class.getName();
	
	public static SplashFragment create() {
		SplashFragment fragment = new SplashFragment();
		return fragment;
    }
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = (ViewGroup) inflater.inflate(R.layout.fragment_splash, container, false);
    	
    	slideUpFragment();
    	toggle(true, false, DIRECTION.TOP);
    	
		return view;
	}
	

	@Override
	protected void enableEditText() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void disableEditText() {
		// TODO Auto-generated method stub
	}
}
