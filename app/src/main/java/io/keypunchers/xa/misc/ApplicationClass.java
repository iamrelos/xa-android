package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.keypunchers.xa.R;
import io.keypunchers.xa.models.UserProfile;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VolleySingleton.instantiate(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean hasCredentials = prefs.contains(getString(R.string.XA_PASSWORD)) && prefs.contains(getString(R.string.XA_PASSWORD));

        if (hasCredentials) {
            UserProfile profile = new UserProfile();
            profile.setUsername(prefs.getString(getString(R.string.XA_USERNAME), null));
            profile.setPassword(prefs.getString(getString(R.string.XA_PASSWORD), null));
            profile.setSignature(prefs.getString(getString(R.string.COMMENT_SIGNATURE), getString(R.string.default_signature)));

            Singleton.getInstance().setUserProfile(profile);
        }
    }
}
