package com.felicekarl.foragingtech.views.fragments;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.listeners.ConfiguringPathListener;
import com.felicekarl.foragingtech.listeners.ControllerNavigatingFragmentButtonListener;
import com.felicekarl.foragingtech.listeners.UpdateConfiguringPathListener;
import com.felicekarl.foragingtech.listeners.UpdateControllerNavigatingFragmentButtonListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ControllerNavigatingFragment extends BaseFragment implements OnClickListener,
		UpdateConfiguringPathListener, UpdateControllerNavigatingFragmentButtonListener {
	private static final String TAG = ControllerNavigatingFragment.class.getSimpleName();
	
	private ConfiguringPathListener mConfiguringPathListener;
	private ControllerNavigatingFragmentButtonListener mControllerNavigatingFragmentButtonListener;
	private Button btn_reset_path;
	private Button btn_set_path;
	private Button btn_take_off;
	private Button btn_start_navigating;
	
	public enum NAVIGATINGMODE {
		CONFIGURING, CONFIGURED, NAVIGATING
	}
	
	private NAVIGATINGMODE mode = NAVIGATINGMODE.CONFIGURING;
	
	public NAVIGATINGMODE getNavigatingMode() {
		return mode;
	}
	
	public void setNavigatingMode(NAVIGATINGMODE mode) {
		this.mode = mode;
		if (mode.equals(NAVIGATINGMODE.CONFIGURING)) {
			getActivity().runOnUiThread(new Runnable(){
    			@Override
    			public void run() {
    				btn_start_navigating.setText("Start Navigating");
    			}
        	});
		} else if (mode.equals(NAVIGATINGMODE.CONFIGURED)) {
			getActivity().runOnUiThread(new Runnable(){
    			@Override
    			public void run() {
    				btn_start_navigating.setText("Start Navigating");
    			}
        	});
		} else if (mode.equals(NAVIGATINGMODE.NAVIGATING)) {
			getActivity().runOnUiThread(new Runnable(){
    			@Override
    			public void run() {
    				btn_start_navigating.setText("Stop Navigating");
    			}
        	});
		}
	}
	
	public ControllerNavigatingFragment() {
    	
    }
	
	public static ControllerNavigatingFragment create() {
		return new ControllerNavigatingFragment();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	view = (ViewGroup) inflater.inflate(R.layout.fragment_controller_navigating, container, false);
    	
    	btn_reset_path = (Button) view.findViewById(R.id.btn_reset_path);
    	btn_reset_path.setOnClickListener(this);
    	btn_set_path = (Button) view.findViewById(R.id.btn_set_path);
    	btn_set_path.setOnClickListener(this);
    	btn_take_off = (Button) view.findViewById(R.id.btn_take_off);
    	btn_take_off.setOnClickListener(this);
    	btn_start_navigating = (Button) view.findViewById(R.id.btn_start_navigating);
    	btn_start_navigating.setOnClickListener(this);
    	
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

	@Override
	public void resetFragment() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_reset_path:
			if (mConfiguringPathListener != null)
				mConfiguringPathListener.resetPath();
				mode = NAVIGATINGMODE.CONFIGURING;
			break;
		case R.id.btn_set_path:
			if (mConfiguringPathListener != null)
				mConfiguringPathListener.savePath();
				mode = NAVIGATINGMODE.CONFIGURED;
			break;
		case R.id.btn_take_off:
			if (mControllerNavigatingFragmentButtonListener != null)
				mControllerNavigatingFragmentButtonListener.toggleTakeOffLanding();
			break;
		case R.id.btn_start_navigating:
			if (mControllerNavigatingFragmentButtonListener != null)
				mControllerNavigatingFragmentButtonListener.toggleStartStopNavigating();
			break;
		}
	}

	@Override
	public void updateConfiguringPathListener(ConfiguringPathListener mConfiguringPathListener) {
		this.mConfiguringPathListener = mConfiguringPathListener;
	}

	@Override
	public void updateControllerNavigatingFragmentButtonListener(
			ControllerNavigatingFragmentButtonListener mControllerNavigatingFragmentButtonListener) {
		this.mControllerNavigatingFragmentButtonListener = mControllerNavigatingFragmentButtonListener;
	}

	public void setIsFlying(boolean isFlying) {
		if (isFlying) {
			getActivity().runOnUiThread(new Runnable(){
				@Override
				public void run() {
					btn_take_off.setText("Landing");
				}
	    	});
		} else {
			getActivity().runOnUiThread(new Runnable(){
				@Override
				public void run() {
					btn_take_off.setText("Take Off");
				}
	    	});
		}
		
	}

}
