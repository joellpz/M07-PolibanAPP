package m07.joellpz.poliban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    ImageView logo, logo_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logoC_splash);
        logo.setVisibility(View.GONE);

        logo_bank = findViewById(R.id.logoB_splash);
        logo_bank.setVisibility(View.GONE);

        Objects.requireNonNull(getSupportActionBar()).hide();
        new Handler().postDelayed(() -> {
            Animation fadeInAll = new AlphaAnimation(0, 1);  // the 1, 0 here notifies that we want the opacity to go from opaque (1) to transparent (0)
            Animation fadeInBank = new AlphaAnimation(0, 1);  // the 1, 0 here notifies that we want the opacity to go from opaque (1) to transparent (0)

            fadeInAll.setInterpolator(new AccelerateInterpolator());
            fadeInBank.setInterpolator(new AccelerateInterpolator());

            fadeInAll.setStartOffset(900); // Start fading out after 500 milli seconds
            fadeInAll.setDuration(800);
            fadeInBank.setDuration(800);

            logo_bank.setVisibility(View.VISIBLE);
            logo.setVisibility(View.VISIBLE);
            logo_bank.startAnimation(fadeInBank);
            logo.startAnimation(fadeInAll);
        }, 1600);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(i);
        },4000);
    }
}