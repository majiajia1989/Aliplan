package com.mjj.aliplan;

import com.mjj.aliplan.KeyboardConfigDialogPreference;
import com.mjj.aliplan.PreferenceConstants;
import com.mjj.aliplan.YesNoDialogPreference;
import com.mjj.aliplan.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;
/**
 * 
 * @author jiajia
 *
 */
public class SetPreferencesActivity extends PreferenceActivity implements
		YesNoDialogPreference.YesNoDialogListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
		getPreferenceManager().setSharedPreferencesName(
				PreferenceConstants.PREFERENCE_NAME);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		Preference eraseGameButton = getPreferenceManager().findPreference(
				"erasegame");
		if (eraseGameButton != null) {
			YesNoDialogPreference yesNo = (YesNoDialogPreference) eraseGameButton;
			yesNo.setListener(this);
		}

		Preference configureKeyboardPref = getPreferenceManager()
				.findPreference("keyconfig");
		if (configureKeyboardPref != null) {
			KeyboardConfigDialogPreference config = (KeyboardConfigDialogPreference) configureKeyboardPref;
			config.setPrefs(getSharedPreferences(
					PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE));
			config.setContext(this);
		}

		if (getIntent().getBooleanExtra("controlConfig", false)) {
			PreferenceScreen controlConfig = (PreferenceScreen) getPreferenceManager()
					.findPreference("controlConfigScreen");
			if (controlConfig != null) {
				setPreferenceScreen(controlConfig);
			}
		}
	}

	public void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			SharedPreferences prefs = getSharedPreferences(
					PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.remove(PreferenceConstants.PREFERENCE_LEVEL_ROW);
			editor.remove(PreferenceConstants.PREFERENCE_LEVEL_INDEX);
			editor.remove(PreferenceConstants.PREFERENCE_LEVEL_COMPLETED);
			editor.remove(PreferenceConstants.PREFERENCE_LINEAR_MODE);
			editor.remove(PreferenceConstants.PREFERENCE_TOTAL_GAME_TIME);
			editor.remove(PreferenceConstants.PREFERENCE_PEARLS_COLLECTED);
			editor.remove(PreferenceConstants.PREFERENCE_PEARLS_TOTAL);
			editor.remove(PreferenceConstants.PREFERENCE_ROBOTS_DESTROYED);
			editor.remove(PreferenceConstants.PREFERENCE_DIFFICULTY);

			editor.commit();
			Toast.makeText(this, R.string.saved_game_erased_notification,
					Toast.LENGTH_SHORT).show();
		}
	}
}
