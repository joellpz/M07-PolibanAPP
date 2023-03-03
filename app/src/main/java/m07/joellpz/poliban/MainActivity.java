package m07.joellpz.poliban;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

import m07.joellpz.poliban.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarMain.toolbar.setTitle("");
//        binding.appBarMain.toolbar.setVisibility(View.GONE);
//        binding.appBarMain.contentMain.bottomMainMenu.setVisibility(View.GONE);

        NavController navController = ((NavHostFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main))).getNavController();
        binding.appBarMain.toolbar.findViewById(R.id.profileAppBarImage).setOnClickListener(l -> navController.navigate(R.id.profileFragment));
        NavigationUI.setupWithNavController(binding.appBarMain.contentMain.bottomMainMenu, navController);

        setSupportActionBar(binding.appBarMain.toolbar);

    }

    //TO DO Fondo delete account
    //TO DO Margin IbanMain

    
    //TODO Invest Page
    //TO DO Make fragment pay good
    //TO DO Connect Buttons From Iban to Pay fragment
    //TO DO Connectar MapFragment al hacer click largo en Map
    //TODO Permitir poner Tarjeta en el Wallet
    //TO DO Lista de Futures en BankAccount y Mostrarlos en el Calendar Explicits


    // https://www.geeksforgeeks.org/flexbox-layout-in-android/
    // https://stackoverflow.com/questions/31250174/css-flexbox-difference-between-align-items-and-align-content
}