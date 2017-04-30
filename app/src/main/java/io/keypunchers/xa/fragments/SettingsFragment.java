package io.keypunchers.xa.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import io.keypunchers.xa.R;

public class SettingsFragment extends Fragment {
    private Spinner mPlatformSpinner;
    private SharedPreferences mPrefs;
    private Spinner mDefaultHomeSpinner;
    private Spinner mEndlessScrollerMaxSpinner;
    private SwitchCompat mHighImageQuality;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mPlatformSpinner = (Spinner) view.findViewById(R.id.spinner_settings_default_platform);
        mPlatformSpinner.setSelection(mPrefs.getInt("DEFAULT_PLATFORM_POSITION", 0));

        mDefaultHomeSpinner = (Spinner) view.findViewById(R.id.spinner_settings_default_home);
        mDefaultHomeSpinner.setSelection(mPrefs.getInt("DEFAULT_HOME_POSITION", 0));

        mEndlessScrollerMaxSpinner = (Spinner) view.findViewById(R.id.spinner_settings_scroll_max_items);
        mEndlessScrollerMaxSpinner.setSelection(mPrefs.getInt("ENDLESS_SCROLLER_MAX_ITEMS_POSITION", 0));

        mHighImageQuality = (SwitchCompat) view.findViewById(R.id.sw_settings_image_quality);
        mHighImageQuality.setChecked(mPrefs.getBoolean("HIGH_IMAGE_QUALITY", true));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.ab_settings_title);

        setupPlatformSpinner();

        setupDefaultHomeSpinner();

        setupEndlessScrollerMaxItemsSpinner();

        setupHighImageQuality();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);

        menu.removeItem(R.id.main_menu_donate);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_item_settings_reset:
                resetToDefault();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupEndlessScrollerMaxItemsSpinner() {
        mEndlessScrollerMaxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int max = Integer.parseInt(mEndlessScrollerMaxSpinner.getAdapter().getItem(position).toString());

                mPrefs.edit()
                        .putInt("ENDLESS_SCROLLER_MAX_ITEMS", max)
                        .putInt("ENDLESS_SCROLLER_MAX_ITEMS_POSITION", position)
                        .apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupDefaultHomeSpinner() {
        mDefaultHomeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPrefs.edit()
                        .putInt("DEFAULT_HOME_POSITION", position)
                        .apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupPlatformSpinner() {
        mPlatformSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String platform = null;

                switch (position) {
                    case 0:
                        platform = "xbox-one";
                        break;
                    case 1:
                        platform = "retail";
                        break;
                    case 2:
                        platform = "arcade";
                        break;
                    case 3:
                        platform = "japanese";
                        break;
                    case 4:
                        platform = "win8";
                        break;
                    case 5:
                        platform = "wp7";
                        break;
                    case 6:
                        platform = "pc";
                        break;
                }

                mPrefs.edit()
                        .putString("DEFAULT_PLATFORM", platform)
                        .putInt("DEFAULT_PLATFORM_POSITION", position)
                        .apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupHighImageQuality() {
        mHighImageQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefs.edit().putBoolean("HIGH_IMAGE_QUALITY", mHighImageQuality.isChecked()).apply();
            }
        });
    }

    public void resetToDefault() {
        mPrefs.edit().clear().apply();
        mPrefs.edit().putBoolean("DRAWER_LEARNED", true).apply();
        mPlatformSpinner.setSelection(0);
        mDefaultHomeSpinner.setSelection(0);
        mEndlessScrollerMaxSpinner.setSelection(0);
        mHighImageQuality.setChecked(true);
    }
}
