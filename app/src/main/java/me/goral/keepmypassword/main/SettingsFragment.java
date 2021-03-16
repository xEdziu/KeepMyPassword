package me.goral.keepmypassword.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import me.goral.keepmypassword.MainActivity;
import me.goral.keepmypassword.R;
import me.goral.keepmypassword.utils.asyncTasks.ManageAccount;

public class SettingsFragment extends PreferenceFragmentCompat {

    String uid;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_fragment, rootKey);

        Bundle bundle = getArguments();
        if (bundle != null)
            uid = bundle.getString("uid");

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

        Preference delPasswords = findPreference("delete_passwords");
        delPasswords.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Are you sure?This action cannot be undone.");
                alert.setMessage("Are you sure?");
                alert.setPositiveButton("YES, DELETE PASSWORDS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ManageAccount async = new ManageAccount(new ManageAccount.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                String[] result = output.split(";");
                                Toast.makeText(getActivity(), result[1], Toast.LENGTH_SHORT).show();
                            }
                        });
                        async.setParentActivity(getActivity());
                        async.execute("1", uid);
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
                return true;
            }
        });

        Preference delAccount = findPreference("delete_account");
        delAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("This action cannot be undone.");
                alert.setMessage("Are you sure?");
                alert.setPositiveButton("YES, DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ManageAccount async = new ManageAccount(new ManageAccount.AsyncResponse() {
                            @Override
                            public void processFinish(String output) {
                                String[] result = output.split(";");
                                if (result[0].equals("true")){
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    getActivity().finish();
                                    startActivity(intent);
                                } else
                                    Toast.makeText(getActivity(), result[1], Toast.LENGTH_SHORT).show();
                            }
                        });
                        async.setParentActivity(getActivity());
                        async.execute("2", uid);
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
                return true;
            }
        });
    }
}
