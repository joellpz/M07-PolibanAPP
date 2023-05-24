package m07.joellpz.poliban;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import m07.joellpz.poliban.databinding.ActivityMainBinding;


/**
 * The MainActivity class represents the main activity of the application.
 * It serves as the entry point for the app and hosts the navigation graph.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Creates and initializes the activity.
     *
     * @param savedInstanceState the saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarMain.toolbar.setTitle("");

        NavController navController = ((NavHostFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main))).getNavController();
        binding.appBarMain.toolbar.findViewById(R.id.profileAppBarImage).setOnClickListener(l -> navController.navigate(R.id.profileFragment));
        NavigationUI.setupWithNavController(binding.appBarMain.contentMain.bottomMainMenu, navController);

        setSupportActionBar(binding.appBarMain.toolbar);

    }

    // TO DO: Implement background for delete account
    // TO DO: Adjust margin in IbanMain

    // TODO: Implement Invest Page
    // TO DO: Improve pay fragment layout
    // TO DO: Connect buttons from Iban to Pay fragment
    // TO DO: Connect MapFragment on long click on map
    // TO DO: Allow adding cards to the Wallet
    // TO DO: Implement list of futures in BankAccount and display them in the calendar
    // TO DO: Implement to see the Transactions in the Map and let see the info from that Transaction https://github.com/googlemaps/android-maps-utils

    // Useful resources:
    // - Flexbox layout in Android: https://www.geeksforgeeks.org/flexbox-layout-in-android/
    // - Difference between align-items and align-content in CSS flexbox: https://stackoverflow.com/questions/31250174/css-flexbox-difference-between-align-items-and-align-content
}
