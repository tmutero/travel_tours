package tansoft.travel_tours.activity.Preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import tansoft.travel_tours.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
