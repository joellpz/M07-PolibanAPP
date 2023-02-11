package m07.joellpz.poliban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.Objects;

import m07.joellpz.poliban.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m07.joellpz.poliban.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarMain.toolbar.setTitle("");
        binding.appBarMain.toolbar.setVisibility(View.GONE);
        binding.appBarMain.contentMain.bottomMainMenu.setVisibility(View.GONE);

        NavController navController = ((NavHostFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main))).getNavController();
        NavigationUI.setupWithNavController(binding.appBarMain.contentMain.bottomMainMenu, navController);

        setSupportActionBar(binding.appBarMain.toolbar);


    }
    // https://www.geeksforgeeks.org/flexbox-layout-in-android/
    // https://stackoverflow.com/questions/31250174/css-flexbox-difference-between-align-items-and-align-content
}