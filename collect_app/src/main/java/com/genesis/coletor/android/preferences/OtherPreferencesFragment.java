package com.genesis.coletor.android.preferences;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.text.InputFilter;

import com.genesis.coletor.android.preferences.filters.ControlCharacterFilter;
import com.genesis.coletor.android.preferences.filters.WhitespaceFilter;

import com.genesis.coletor.android.R;
import com.genesis.coletor.android.preferences.filters.ControlCharacterFilter;
import com.genesis.coletor.android.preferences.filters.WhitespaceFilter;


public class OtherPreferencesFragment extends AggregatePreferencesFragment implements Preference.OnPreferenceChangeListener {

    protected EditTextPreference mSubmissionUrlPreference;
    protected EditTextPreference mFormListUrlPreference;


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.other_preferences);

        mFormListUrlPreference = (EditTextPreference) findPreference(
                PreferenceKeys.KEY_FORMLIST_URL);
        mSubmissionUrlPreference = (EditTextPreference) findPreference(
                PreferenceKeys.KEY_SUBMISSION_URL);

        InputFilter[] filters = {new ControlCharacterFilter(), new WhitespaceFilter()};

        mServerUrlPreference.getEditText().setFilters(filters);

        mFormListUrlPreference.setOnPreferenceChangeListener(this);
        mFormListUrlPreference.setSummary(mFormListUrlPreference.getText());
        mFormListUrlPreference.getEditText().setFilters(filters);

        mSubmissionUrlPreference.setOnPreferenceChangeListener(this);
        mSubmissionUrlPreference.setSummary(mSubmissionUrlPreference.getText());
        mSubmissionUrlPreference.getEditText().setFilters(filters);

    }


    /**
     * Generic listener that sets the summary to the newly selected/entered
     * value
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary((CharSequence) newValue);
        return true;
    }


}
