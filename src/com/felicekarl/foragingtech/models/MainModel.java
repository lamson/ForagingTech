package com.felicekarl.foragingtech.models;

import android.content.Context;

public class MainModel implements IModel {
	private static final String TAG = MainModel.class.getSimpleName();
	private Context context;
	
	public MainModel(Context context) {
		this.context = context;
	}
}
