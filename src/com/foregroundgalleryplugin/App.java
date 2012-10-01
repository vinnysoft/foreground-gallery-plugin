package com.foregroundgalleryplugin;

import org.apache.cordova.DroidGap;

import android.os.Bundle;

/** 
 * App class.
 * Opens index.html file to test the gallery.
 */
public class App extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.loadUrl("file:///android_asset/www/index.html");
	}
}
