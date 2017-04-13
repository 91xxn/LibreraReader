package com.foobnix.android.utils;

import com.foobnix.pdf.info.wrapper.AppState;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class Keyboards {
	public static void hideAlways(Activity context) {
		context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public static void close(Activity context) {
		InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		View currentFocus = context.getCurrentFocus();
		if (currentFocus != null) {
			inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static void close(View currentFocus) {
		InputMethodManager inputManager = (InputMethodManager) currentFocus.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
		inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void hideNavigation(Activity a) {
		try {
			if (!AppState.get().isFullScreen) {
				return;
			}
			if (Build.VERSION.SDK_INT >= 16) {
				a.getWindow().getDecorView().setSystemUiVisibility(
						View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
								| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
								| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			}
		} catch (Exception e) {
			LOG.e(e);
		}

	}

}