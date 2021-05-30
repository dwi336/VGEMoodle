/*
 * Copyright (C) 2020 Gaukler Faun
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.github.dwi336.vgemoodle;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.github.dwi336.vgemoodle.R;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

public class Activity_Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
        setTitle(R.string.menu_more_settings);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new SettingsFragment())
                .commit();
    }

    @SuppressWarnings("ConstantConditions")
    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.user_settings, rootKey);

            findPreference("settings_help").setOnPreferenceClickListener(preference -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.toast_cancel, (dialog, id) -> dialog.cancel());
                builder.setTitle(R.string.dialog_help_title);
                builder.setMessage(Class_Helper.textSpannable(requireActivity().getString(R.string.dialog_help_text)));
                AlertDialog dialog = builder.create();
                dialog.show();
                ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                return false;
            });

            findPreference("settings_license").setOnPreferenceClickListener(preference -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setPositiveButton(R.string.toast_cancel, (dialog, id) -> dialog.cancel());
                builder.setTitle(R.string.dialog_license_title);
                builder.setMessage(Class_Helper.textSpannable(requireActivity().getString(R.string.dialog_license_text)));
                AlertDialog dialog = builder.create();
                dialog.show();
                ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                return false;
            });

            findPreference("settings_security_moodle").setOnPreferenceClickListener(preference -> {
                Class_Helper.setLoginData (getActivity());
                return false;
            });

            findPreference("settings_security_pin").setOnPreferenceClickListener(preference -> {
                final Activity activity = getActivity();
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(activity));
                final String password = sharedPref.getString("settings_security_pin", "");
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                View dialogView = View.inflate(activity, R.layout.dialog_edit_pin, null);

                final EditText pass_userPW = dialogView.findViewById(R.id.pass_userPin);
                pass_userPW.setText(password);

                builder.setView(dialogView);
                builder.setTitle(R.string.settings_security_pin);
                builder.setPositiveButton(R.string.toast_yes, (dialog, whichButton) -> {
                    String inputTag = pass_userPW.getText().toString().trim();
                    sharedPref.edit().putString("settings_security_pin", inputTag).apply();
                });
                builder.setNegativeButton(R.string.toast_cancel, (dialog, whichButton) -> dialog.cancel());
                final AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Class_Helper.switchToActivity(Activity_Settings.this, Activity_Main.class);
        }

        return super.onOptionsItemSelected(item);
    }
}
