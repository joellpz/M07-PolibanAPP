package m07.joellpz.poliban.main;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import m07.joellpz.poliban.R;
/**
 * A fragment for displaying application settings.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    /**
     * Called to create the preferences hierarchy for the SettingsFragment.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @param rootKey            If non-null, this preference fragment should be rooted at the PreferenceScreen with this key.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    /**
     * Called when the view creation is complete.
     *
     * @param view               The created view
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}