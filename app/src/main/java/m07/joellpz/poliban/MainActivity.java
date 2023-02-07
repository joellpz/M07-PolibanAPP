package m07.joellpz.poliban;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import m07.joellpz.poliban.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarMain.toolbar.setTitle("");
        binding.appBarMain.toolbar.setVisibility(View.GONE);
        setSupportActionBar(binding.appBarMain.toolbar);


    }
}