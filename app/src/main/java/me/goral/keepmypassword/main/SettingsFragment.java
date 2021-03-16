package me.goral.keepmypassword.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import me.goral.keepmypassword.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_fragment, rootKey);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("night", Context.MODE_PRIVATE);
        Boolean flag = sharedPreferences.getBoolean("night_mode", false);
        SwitchPreferenceCompat switchDayNight = findPreference("switch_day_night");

        assert switchDayNight != null;
        if (flag)
            switchDayNight.setIcon(R.drawable.ic_dark_mode_24);
        else
            switchDayNight.setIcon(R.drawable.ic_light_mode_24);

        switchDayNight.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode", (boolean) newValue);
                editor.apply();
                getActivity().recreate();
                return true;
            }
        });
    }

}
