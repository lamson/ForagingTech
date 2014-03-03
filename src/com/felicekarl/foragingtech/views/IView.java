package com.felicekarl.foragingtech.views;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.view.Surface;

import com.felicekarl.foragingtech.listeners.*;

public interface IView extends UpdateFlipForwardButtonListener, UpdateFlipBackwardButtonListener, 
		UpdateTakePhotoListener, UpdateDroneCommandListener {
	public void setView(TypeView type);
	public Bitmap getCameraBitmap();
	public Bitmap getImageBitmap();
	public Surface getCameraSurface();
	
	public enum TypeView {
		SPLASH, MENU, FLYINGMODE, NAVIGATINGMODE
	}
}
