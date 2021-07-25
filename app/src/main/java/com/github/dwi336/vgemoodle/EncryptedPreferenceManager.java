/*
 * Copyright (C) 2021 Dietmar Wippig <dwi336.dev@gmail.com>
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

import java.io.IOException;
import java.security.GeneralSecurityException;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme;
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme;
import androidx.security.crypto.MasterKey;

public class EncryptedPreferenceManager {
    public static EncryptedPreferenceManager mInstance;

    private static SharedPreferences mEncSharedPrefs;

    private EncryptedPreferenceManager(Context context) {

    try {
        MasterKey masterKey = new MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build();
        mEncSharedPrefs = EncryptedSharedPreferences.create(
            context,
            "secret_shared_prefs",
            masterKey,
            PrefKeyEncryptionScheme.AES256_SIV,
            PrefValueEncryptionScheme.AES256_GCM);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            mEncSharedPrefs = null;
        }
    }

    public static SharedPreferences getEncSharedPreferences(Context context) {
        if (mInstance == null) {
            mInstance = new EncryptedPreferenceManager(context);
        }
        return mEncSharedPrefs;
    }

}
