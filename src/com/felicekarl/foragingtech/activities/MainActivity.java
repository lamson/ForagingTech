package com.felicekarl.foragingtech.activities;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import com.felicekarl.foragingtech.R;
import com.felicekarl.foragingtech.models.MainModel;
import com.felicekarl.foragingtech.presenters.MainPresenter;
import com.felicekarl.foragingtech.views.MainView;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MainActivity extends FragmentActivity {
	public static final boolean VERBOSE = true;
	private static final String TAG = MainActivity.class.getSimpleName();

	private MainPresenter presenter;
	private MainView view;
	private MainModel model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view = new MainView(this);
		model = new MainModel(this);
		presenter = new MainPresenter(this, view, model);

		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
	}

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
				if (VERBOSE)	Log.i(TAG, "OpenCV loaded successfully");
				break;
			default:
				super.onManagerConnected(status);
				break;
			}
		}
	};
}
