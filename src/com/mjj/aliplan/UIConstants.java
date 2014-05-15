
package com.mjj.aliplan;

import java.lang.reflect.Method;

import android.app.Activity;
/**
 * UI常量
 * @author jiajia
 *
 */
public class UIConstants {
	public static Method mOverridePendingTransition;

	static {
		try {
			mOverridePendingTransition = Activity.class.getMethod(
					"overridePendingTransition", new Class[] { Integer.TYPE,
							Integer.TYPE });
			/* success, this is a newer device */
		} catch (NoSuchMethodException nsme) {
			/* failure, must be older device */
		}
	};
}
