package com.felicekarl.foragingtech.views;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.activities.MainActivity;
import com.felicekarl.foragingtech.listeners.DroneCommandListener;
import com.felicekarl.foragingtech.listeners.FlipBackwardButtonListener;
import com.felicekarl.foragingtech.listeners.FlipForwardButtonListener;
import com.felicekarl.foragingtech.listeners.TakePhotoListener;
import com.felicekarl.foragingtech.views.fragments.CameraFragment;
import com.felicekarl.foragingtech.views.fragments.ContentFlyingFragment;
import com.felicekarl.foragingtech.views.fragments.ContentNavigatingFragment;
import com.felicekarl.foragingtech.views.fragments.PagerFragment;
import com.felicekarl.foragingtech.views.fragments.SplashFragment;
import com.felicekarl.foragingtech.views.fragments.BaseFragment.DIRECTION;
import com.felicekarl.foragingtech.views.fragments.CameraFragment.IMAGEMODE;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;

public class MainView implements IView {
	private static final String TAG = MainView.class.getSimpleName();
	
	private Context context;
	private TypeView curTypeView;
	private FragmentManager mFragmentManager;
	private SplashFragment mSplashFragment;
	private PagerFragment mPagerFragment;
	private ContentFlyingFragment mContentFlyingFragment;
	private ContentNavigatingFragment mContentNavigatingFragment;
	private CameraFragment mCameraFragment;
	
	
	
	
	public MainView(Context context) {
		this.context = context;
		this.mFragmentManager = ((MainActivity) context).getFragmentManager();
		/* Initialize curTypeView as Splash Screen */
		curTypeView = TypeView.SPLASH;
		initFragments();
	}

	private void initFragments() {
		/* add pager screen fragment */
		mPagerFragment = new PagerFragment();
		mFragmentManager.beginTransaction().add(R.id.main, mPagerFragment).commit();
		/* add splash screen fragment */
		mSplashFragment = SplashFragment.create();
		mFragmentManager.beginTransaction().add(R.id.main, mSplashFragment).commit();
		/* add content flying mode fragment */
		mContentFlyingFragment = ContentFlyingFragment.create();
		mFragmentManager.beginTransaction().add(R.id.main, mContentFlyingFragment).commit();
		/* add content navigating mode fragment */
		mContentNavigatingFragment = ContentNavigatingFragment.create();
		mFragmentManager.beginTransaction().add(R.id.main, mContentNavigatingFragment).commit();
		/* add camera fragment */
		mCameraFragment = CameraFragment.create();
		mFragmentManager.beginTransaction().add(R.id.main, mCameraFragment).commit();
	}

	@Override
	public void setView(TypeView type) {
		if(curTypeView.equals(TypeView.SPLASH) && type.equals(TypeView.MENU)) {
			curTypeView = TypeView.MENU;
			mPagerFragment.toggle(true, true, DIRECTION.TOP);
			mSplashFragment.toggle(false, true, DIRECTION.BOTTOM);
			mContentFlyingFragment.toggle(false, false, DIRECTION.TOP);
			mContentNavigatingFragment.toggle(false, false, DIRECTION.TOP);
			mCameraFragment.toggle(false, false, DIRECTION.TOP);
			
		}
		if(curTypeView.equals(TypeView.MENU) && type.equals(TypeView.FLYINGMODE)) {
			curTypeView = TypeView.FLYINGMODE;
			mPagerFragment.toggle(false, true, DIRECTION.BOTTOM);
			mSplashFragment.toggle(false, false, DIRECTION.TOP);
			mContentFlyingFragment.toggle(true, true, DIRECTION.TOP);
			mContentNavigatingFragment.toggle(false, false, DIRECTION.TOP);
			mCameraFragment.toggle(true, true, DIRECTION.TOP);
			
			mCameraFragment.setFullScreen();
			mCameraFragment.setFrameColor("#f0a30a");
			mCameraFragment.setImageMode(IMAGEMODE.CANNY);
		} else if(curTypeView.equals(TypeView.MENU) && type.equals(TypeView.NAVIGATINGMODE)) {
			curTypeView = TypeView.NAVIGATINGMODE;
			mPagerFragment.toggle(false, true, DIRECTION.BOTTOM);
			mSplashFragment.toggle(false, false, DIRECTION.TOP);
			mContentFlyingFragment.toggle(false, false, DIRECTION.TOP);
			mContentNavigatingFragment.toggle(true, true, DIRECTION.TOP);
			mCameraFragment.toggle(true, true, DIRECTION.TOP);
			
			mCameraFragment.setSmallScreen();
			mCameraFragment.setFrameColor("#6a00ff");
		}
		if(curTypeView.equals(TypeView.FLYINGMODE) && type.equals(TypeView.MENU)) {
			curTypeView = TypeView.MENU;
			mPagerFragment.toggle(true, true, DIRECTION.BOTTOM);
			mSplashFragment.toggle(false, false, DIRECTION.TOP);
			mContentFlyingFragment.toggle(false, true, DIRECTION.TOP);
			mContentNavigatingFragment.toggle(false, false, DIRECTION.TOP);
			mCameraFragment.toggle(false, true, DIRECTION.TOP);
		} else if(curTypeView.equals(TypeView.NAVIGATINGMODE) && type.equals(TypeView.MENU)) {
			curTypeView = TypeView.MENU;
			mPagerFragment.toggle(true, true, DIRECTION.BOTTOM);
			mSplashFragment.toggle(false, false, DIRECTION.TOP);
			mContentFlyingFragment.toggle(false, false, DIRECTION.TOP);
			mContentNavigatingFragment.toggle(false, true, DIRECTION.TOP);
			mCameraFragment.toggle(false, true, DIRECTION.TOP);
		}
	}
	

	@Override
	public void updateFlipForwardButtonListener(FlipForwardButtonListener mFlipForwardButtonListener) {
		mPagerFragment.updateFlipForwardButtonListener(mFlipForwardButtonListener);
	}

	@Override
	public void updateFlipBackwardButtonListener(FlipBackwardButtonListener mFlipBackwardButtonListener) {
		mContentFlyingFragment.updateFlipBackwardButtonListener(mFlipBackwardButtonListener);
		mContentNavigatingFragment.updateFlipBackwardButtonListener(mFlipBackwardButtonListener);
	}
	
	@Override
	public void updateTakePhotoListener(TakePhotoListener mTakePhotoListener) {
		mContentFlyingFragment.updateTakePhotoListener(mTakePhotoListener);
		// TODO: add listener for navigating mode
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
	public Bitmap getImageBitmap() {
		if (mCameraFragment != null)	return mCameraFragment.getProcessedImage();
		return null;
	}

	@Override
	public void updateDroneCommandListener(DroneCommandListener mDroneCommandListener) {
		mContentFlyingFragment.updateDroneCommandListener(mDroneCommandListener);
		// TODO: add listener for navigating mode
	}
}
