package de.baumann.hhsmoodle.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import de.baumann.hhsmoodle.HHS_Browser;

public class Start extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String startURL = sharedPref.getString("favoriteURL", "\"https://moodle.huebsch.ka.schule-bw.de/moodle/");

        Intent intent = new Intent(Start.this, HHS_Browser.class);
        intent.putExtra("url", startURL);
        startActivityForResult(intent, 100);
        finish();
    }
}