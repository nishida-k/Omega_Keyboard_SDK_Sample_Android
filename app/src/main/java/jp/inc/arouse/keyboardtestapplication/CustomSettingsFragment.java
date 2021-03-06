package jp.inc.arouse.keyboardtestapplication;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.text.TextUtils;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.common.collect.Lists;
import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.fragment.dialog.alert.AlertDialogFragment;
import com.omega.keyboard.sdk.fragment.settings.SettingsFragment;
import com.omega.keyboard.sdk.mozc.preference.PreferencePage;
import com.takisoft.fix.support.v7.preference.EditTextPreference;

import java.util.ArrayList;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/10/24.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class CustomSettingsFragment extends SettingsFragment {

	public static CustomSettingsFragment newInstance() {
		CustomSettingsFragment fragment = new CustomSettingsFragment();

		ArrayList<Integer> addContents = Lists.newArrayList(
				R.xml.pref_custom_settings_002,
				R.xml.pref_custom_settings_003
		);

		Bundle args = new Bundle();
		args.putInt(SettingsFragment.ARG_CONTAINER_ID, R.id.container);
		args.putIntegerArrayList(SettingsFragment.ARG_ADD_CONTENTS, addContents);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("設定");
	}

	@Override
	protected void updatePreferences() {
		super.updatePreferences();

		Preference preference = findPreference("pref_custom_settings_006");
		if (preference != null) {
			preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					new AlertDialogFragment.Builder()
							.title("カスタム設定 [6]")
							.message("SettingsFragment テスト")
							.positive("OK")
							.negative("キャンセル")
							.build()
							.show(CustomSettingsFragment.this);
					return true;
				}
			});
		}

		preference = findPreference("pref_custom_settings_007");
		if (preference != null) {
			preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					SettingsFragment nextFragment = SettingsFragment.newInstance(R.id.container, PreferencePage.SDK_SOFTWARE_KEYBOARD, Lists.newArrayList(R.xml.pref_custom_settings_001));

					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
					transaction.replace(R.id.container, nextFragment, "pref_custom_settings_007");
					transaction.addToBackStack("pref_custom_settings_007");
					transaction.commit();
					return true;
				}
			});
		}

		preference = findPreference(SharedPrefKey.AD_VIEW_BACKGROUND_COLOR);
		if (preference != null) {
			preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					onClickColorPickerPreference(preference);
					return true;
				}
			});
		}

		preference = findPreference(SharedPrefKey.AD_VIEW_TEXT_COLOR);
		if (preference != null) {
			preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					onClickColorPickerPreference(preference);
					return true;
				}
			});
		}

		preference = findPreference(SharedPrefKey.USER_ID);
		if (preference != null) {
			EditTextPreference editTextPreference = (EditTextPreference)preference;
			editTextPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					String userId = String.valueOf(newValue);

					KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(getContext());
					if (TextUtils.isEmpty(userId)) {
						keyboardSDK.deleteUserId();
					}
					else {
						keyboardSDK.setUserId(userId);
					}
					return true;
				}
			});
		}
	}

	private void onClickColorPickerPreference(Preference preference) {
		final SharedPreferences sharedPreferences = preference.getSharedPreferences();
		final String key = preference.getKey();
		int color = sharedPreferences.getInt(key, Color.WHITE);
		ColorPickerDialogBuilder.with(getContext())
				.setTitle(preference.getTitle().toString())
				.initialColor(color)
				.showColorPreview(true)
				.showAlphaSlider(true)
				.showLightnessSlider(true)
				.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
				.setPositiveButton("決定", new ColorPickerClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
						sharedPreferences.edit()
								.putInt(key, selectedColor)
								.apply();
					}
				})
				.setNegativeButton("キャンセル", null)
				.build()
				.show();
	}
}
