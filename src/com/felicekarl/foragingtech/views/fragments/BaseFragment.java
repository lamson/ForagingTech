package com.felicekarl.foragingtech.views.fragments;

import android.app.Fragment;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;

public abstract class BaseFragment extends Fragment {
	@SuppressWarnings("unused")
	private static final String TAG = BaseFragment.class.getSimpleName();
	
	protected ViewGroup view;
	protected int width;
	protected int height;
	protected static int ANIM_SLIDE_DURATION = 500;
	
	protected void slideUpFragment() {
		ViewTreeObserver vto = view.getViewTreeObserver();
    	vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				width = view.getWidth();
				height = view.getHeight();
				ViewTreeObserver obs = view.getViewTreeObserver();
				obs.removeOnGlobalLayoutListener(this);
				view.setTranslationY(-height);
			}
		});
	}
	
	public void toggle(boolean isOnStage, boolean isAnimation, DIRECTION direction) {
		if (isOnStage) {
			if(isAnimation) {
				if (direction.equals(DIRECTION.TOP)) {
					view.setTranslationY(-height);
					view.animate().translationY(0).setDuration(ANIM_SLIDE_DURATION).withLayer();
				} else if (direction.equals(DIRECTION.BOTTOM)) {
					view.setTranslationY(height);
					view.animate().translationY(0).setDuration(ANIM_SLIDE_DURATION).withLayer();
				} else if (direction.equals(DIRECTION.LEFT)) {
					view.setTranslationX(-width);
					view.animate().translationX(0).setDuration(ANIM_SLIDE_DURATION).withLayer();
				} else if (direction.equals(DIRECTION.RIGHT)) {
					view.setTranslationX(width);
					view.animate().translationX(0).setDuration(ANIM_SLIDE_DURATION).withLayer();
				}
			} else {
				view.animate().translationX(0).setDuration(0).withLayer();
				view.animate().translationY(0).setDuration(0).withLayer();
			}
    		enableEditText();
    	} else {
    		if(isAnimation) {
    			if (direction.equals(DIRECTION.TOP)) {
					view.animate().translationY(-height).setDuration(ANIM_SLIDE_DURATION).withLayer();
				} else if (direction.equals(DIRECTION.BOTTOM)) {
					view.animate().translationY(height).setDuration(ANIM_SLIDE_DURATION).withLayer();
				} else if (direction.equals(DIRECTION.LEFT)) {
					view.animate().translationX(-width).setDuration(ANIM_SLIDE_DURATION).withLayer();
				} else if (direction.equals(DIRECTION.RIGHT)) {
					view.animate().translationX(width).setDuration(ANIM_SLIDE_DURATION).withLayer();
				}
    		} else {
    			if (direction.equals(DIRECTION.TOP)) {
    				view.setTranslationX(0);
    				view.animate().translationY(-height).setDuration(0).withLayer();
				} else if (direction.equals(DIRECTION.BOTTOM)) {
					view.setTranslationX(0);
    				view.animate().translationY(height).setDuration(0).withLayer();
				} else if (direction.equals(DIRECTION.LEFT)) {
					view.animate().translationX(-width).setDuration(0).withLayer();
					view.setTranslationY(0);
				} else if (direction.equals(DIRECTION.RIGHT)) {
					view.animate().translationX(width).setDuration(0).withLayer();
					view.setTranslationY(0);
				}
    		}
    		disableEditText();
    	}
    }
	
	protected void closeVirtualKeyboard() {
		// close virtual keyboard
		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(getActivity().INPUT_METHOD_SERVICE); 
		if ( inputManager != null && getActivity().getCurrentFocus() != null ) {
			inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	protected abstract void enableEditText();
	protected abstract void disableEditText();
	
	public enum DIRECTION {
		TOP, BOTTOM, LEFT, RIGHT
	}
	
	public abstract void resetFragment();
}
