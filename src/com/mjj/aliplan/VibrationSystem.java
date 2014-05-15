package com.mjj.aliplan;

import android.os.Vibrator;
import android.content.Context;
/**
 * 震动
 * @author jiajia
 *
 */
public class VibrationSystem extends BaseObject {

	public VibrationSystem() {
		super();
	}

	@Override
	public void reset() {
	}

	public void vibrate(float seconds) {
		ContextParameters params = sSystemRegistry.contextParameters;
		if (params != null && params.context != null) {
			Vibrator vibrator = (Vibrator) params.context
					.getSystemService(Context.VIBRATOR_SERVICE);
			if (vibrator != null) {
				vibrator.vibrate((int) (seconds * 1000));
			}
		}
	}
}
