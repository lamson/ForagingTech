package com.felicekarl.foragingtech.views;

import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Surface;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.activities.MainActivity;
import com.felicekarl.foragingtech.listeners.CameraFragmentButtonListener;
import com.felicekarl.foragingtech.listeners.ContentActionBarFragmentButtonListener;
import com.felicekarl.foragingtech.listeners.ControllerListener;
import com.felicekarl.foragingtech.listeners.ControllerNavigatingFragmentButtonListener;
import com.felicekarl.foragingtech.listeners.FlipForwardButtonListener;
import com.felicekarl.foragingtech.views.fragments.BaseFragment.DIRECTION;
import com.felicekarl.foragingtech.views.fragments.CameraFragment;
import com.felicekarl.foragingtech.views.fragments.ContentFlyingFragment;
import com.felicekarl.foragingtech.views.fragments.ControllerFragment;
import com.felicekarl.foragingtech.views.fragments.ControllerNavigatingFragment.NAVIGATINGMODE;
import com.felicekarl.foragingtech.views.fragments.MainFragment;
import com.felicekarl.foragingtech.views.fragments.PagerFragment;
import com.felicekarl.foragingtech.views.fragments.SplashFragment;
import com.nutiteq.components.MapPos;

public class MainView implements IView {
	private static final String TAG = MainView.class.getSimpleName();
	
	private Context context;
	private TypeView curTypeView;
	
	private MainFragment mMainFragment;
	private FragmentManager mFragmentManager;
	private SplashFragment mSplashFragment;
	//private PagerFragment mPagerFragment;
	private ContentFlyingFragment mContentFlyingFragment;
	//private ContentNavigatingFragment mContentNavigatingFragment;
	private CameraFragment mCameraFragment;
	private ControllerFragment mControllerFragment;
	
	public MainView(Context context) {
		this.context = context;
		this.mFragmentManager = ((MainActivity) context).getFragmentManager();
		/* Initialize curTypeView as Splash Screen */
		curTypeView = TypeView.SPLASH;
		initFragments();
	}
	
	/**
	 * initialize fragments and add on the fragment manager.
	 */
	private void initFragments() {
	    mMainFragment = MainFragment.create();
	    mFragmentManager.beginTransaction().add(R.id.main, mMainFragment).commit();
		/* add pager screen fragment */
		//mPagerFragment = new PagerFragment();
		//mFragmentManager.beginTransaction().add(R.id.main, mPagerFragment).commit();
		/* add splash screen fragment */
		mSplashFragment = SplashFragment.create();
		mFragmentManager.beginTransaction().add(R.id.main, mSplashFragment).commit();
		/* add content flying mode fragment */
		mContentFlyingFragment = ContentFlyingFragment.create();
		mFragmentManager.beginTransaction().add(R.id.main, mContentFlyingFragment).commit();
		/* add content navigating mode fragment */
		//mContentNavigatingFragment = ContentNavigatingFragment.create();
		//mFragmentManager.beginTransaction().add(R.id.main, mContentNavigatingFragment).commit();
		/* add camera fragment */
		mCameraFragment = CameraFragment.create();
		mFragmentManager.beginTransaction().add(R.id.main, mCameraFragment).commit();
		/* add controller fragment */
		mControllerFragment = ControllerFragment.create();
		mFragmentManager.beginTransaction().add(R.id.main, mControllerFragment).commit();
	}

	@Override
	public void setView(TypeView type) {
		if(curTypeView.equals(TypeView.SPLASH) && type.equals(TypeView.MENU)) {
			curTypeView = TypeView.MENU;
			mMainFragment.toggle(true, false, DIRECTION.TOP);
			// pager (menu) fragment moves on the stage with slide up animation
			//mPagerFragment.toggle(true, true, DIRECTION.TOP);
			// splash fragment moves off the stage with slide up animation
			mSplashFragment.toggle(false, true, DIRECTION.BOTTOM);
			mContentFlyingFragment.toggle(false, false, DIRECTION.TOP);
			//mContentNavigatingFragment.toggle(false, false, DIRECTION.TOP);
			mCameraFragment.toggle(false, false, DIRECTION.TOP);
			mControllerFragment.toggle(false, false, DIRECTION.TOP);
		} else if(curTypeView.equals(TypeView.MENU) && type.equals(TypeView.FLYINGMODE)) {
			curTypeView = TypeView.FLYINGMODE;
			//mCameraFragment.setFrameColor("#333333");
			mCameraFragment.resetFragment();
			
			//mPagerFragment.toggle(false, true, DIRECTION.BOTTOM);
			mSplashFragment.toggle(false, false, DIRECTION.TOP);
			mContentFlyingFragment.toggle(true, true, DIRECTION.TOP);
			//mContentNavigatingFragment.toggle(false, false, DIRECTION.TOP);
			mCameraFragment.toggle(true, true, DIRECTION.TOP);
			mControllerFragment.toggle(true, true, DIRECTION.TOP);
			
			
		} else if(curTypeView.equals(TypeView.MENU) && type.equals(TypeView.NAVIGATINGMODE)) {
			curTypeView = TypeView.NAVIGATINGMODE;
			//mCameraFragment.setCameraSmallScreen();
			mCameraFragment.setFrameColor("#6a00ff");
			
			//mPagerFragment.toggle(false, true, DIRECTION.BOTTOM);
			mSplashFragment.toggle(false, false, DIRECTION.TOP);
			mContentFlyingFragment.toggle(false, false, DIRECTION.TOP);
			//mContentNavigatingFragment.toggle(true, true, DIRECTION.TOP);
			mCameraFragment.toggle(true, true, DIRECTION.TOP);
			mControllerFragment.toggle(false, false, DIRECTION.TOP);
		} else if(curTypeView.equals(TypeView.FLYINGMODE) && type.equals(TypeView.MENU)) {
			curTypeView = TypeView.MENU;
			//mPagerFragment.toggle(true, true, DIRECTION.BOTTOM);
			mSplashFragment.toggle(false, false, DIRECTION.TOP);
			mContentFlyingFragment.toggle(false, true, DIRECTION.TOP);
			//mContentNavigatingFragment.toggle(false, false, DIRECTION.TOP);
			mCameraFragment.toggle(false, true, DIRECTION.TOP);
			mControllerFragment.toggle(false, false, DIRECTION.TOP);
		} else if(curTypeView.equals(TypeView.NAVIGATINGMODE) && type.equals(TypeView.MENU)) {
			curTypeView = TypeView.MENU;
			//mPagerFragment.toggle(true, true, DIRECTION.BOTTOM);
			mSplashFragment.toggle(false, false, DIRECTION.TOP);
			mContentFlyingFragment.toggle(false, false, DIRECTION.TOP);
			//mContentNavigatingFragment.toggle(false, true, DIRECTION.TOP);
			mCameraFragment.toggle(false, true, DIRECTION.TOP);
			mControllerFragment.toggle(false, false, DIRECTION.TOP);
		}
	}
	

	@Override
	public void updateFlipForwardButtonListener(FlipForwardButtonListener mFlipForwardButtonListener) {
	    mMainFragment.updateFlipForwardButtonListener(mFlipForwardButtonListener);
	}
	
	@Override
	public Surface getCameraSurface() {
		if (mCameraFragment != null)	return mCameraFragment.getSurface();
		return null;
	}

	@Override
	public Bitmap getCameraBitmap() {
		if (mCameraFragment != null)	return mCameraFragment.getTextureView().getBitmap();
		return null;
	}
	
	@Override
	public void updateControllerListener(ControllerListener mControllerListener) {
		mControllerFragment.updateControllerListener(mControllerListener);
	}

	@Override
	public void setIsFlying(boolean isFlying) {
		mControllerFragment.setIsFlying(isFlying);
		//mContentNavigatingFragment.setIsFlying(isFlying);
	}

	@Override
	public void updateCameraFragmentButtonListener(CameraFragmentButtonListener mCameraFragmentButtonListener) {
		mCameraFragment.updateCameraFragmentButtonListener(mCameraFragmentButtonListener);
	}

	@Override
	public void updateContentActionBarFragmentButtonListener(
			ContentActionBarFragmentButtonListener mContentActionBarFragmentButtonListener) {
		mContentFlyingFragment.updateContentActionBarFragmentButtonListener(mContentActionBarFragmentButtonListener);
		//mContentNavigatingFragment.updateContentActionBarFragmentButtonListener(mContentActionBarFragmentButtonListener);
	}

	@Override
	public void setIsEmergency(boolean isEmergency) {
		if (curTypeView.equals(TypeView.FLYINGMODE)) {
			mContentFlyingFragment.setIsEmergency(isEmergency);
		}
	}

	@Override
	public void updateControllerNavigatingFragmentButtonListener(
			ControllerNavigatingFragmentButtonListener mControllerNavigatingFragmentButtonListener) {
		//mContentNavigatingFragment.updateControllerNavigatingFragmentButtonListener(mControllerNavigatingFragmentButtonListener);
	}

	@Override
	public NAVIGATINGMODE getNavigatingMode() {
	    return null;
		//return mContentNavigatingFragment.getNavigatingMode();
	}

	@Override
	public void setNavigatingMode(NAVIGATINGMODE mode) {
		//mContentNavigatingFragment.setNavigatingMode(mode);
	}

	@Override
	public List<MapPos> getPath() {
		//return mContentNavigatingFragment.getPath();
	    return null;
	}

	@Override
	public void setDroneCurPos(double lat, double lon) {
		//mContentNavigatingFragment.setDroneCurPos(lat, lon);
	}

	@Override
	public void updateUserCurPos() {
		//mContentNavigatingFragment.updateUserCurPos();
	}

	@Override
	public MapPos getDroneCurPos() {
		//return mContentNavigatingFragment.getDroneCurPos();
	    return null;
	}

    @Override
    public Bitmap getImageBitmap() {
        // TODO Auto-generated method stub
        return null;
    }
}
