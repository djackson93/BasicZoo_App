package com.example.hm8_jackson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class PrefsFragmentSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public PrefsFragmentSettings () {
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Load the preference from an XML resource
        addPreferencesFromResource(R.xml.prefs_fragment_settings);
    }

    @Override
    public void onResume () {
        super.onResume();
        //Set up a listener when a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        //Set up a click listener for company info
        Preference pref;
        pref = getPreferenceScreen().findPreference("key_zoo_info");


        //Click Listener for Zoo Page settings
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Handle our actions on click here
                try {
                    Uri site = Uri.parse("https://houstonpbs.pbslearningmedia.org/resource/evscps.sci.life.safari/zoo-safari/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, site);
                    startActivity(intent);
                }  catch (Exception e) {
                    Log.e("PrefsFragmentSettings", "Browser Failed", e);
                }
                return false;
            }
        });
    }

    //Method for handing a change in preference in terms of our music/sfx being played
    public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key) {

        //Code to handle check box preference for music on/off option
        if (key.equals("key_music_enabled")) {
            boolean b = sharedPreferences.getBoolean("key_music_enabled", true);
            //nested if
            if (b == false) {
                //doubled nested if
                if (Assets.mp != null)
                    Assets.mp.setVolume(0,0);
            }

        } else {
            if (Assets.mp != null)
                Assets.mp.setVolume(1,1);
        }

        //Code to handle check box for sfx on/off option
        if (key.equals("key_sfx_enabled")) {
            boolean b2 = sharedPreferences.getBoolean("key_sfx_enabled", true);
        }

    }
}
