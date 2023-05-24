package m07.joellpz.poliban;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * The SplashScreen class represents the activity that is displayed when the application starts.
 * It shows a splash screen with logos fading in before redirecting to the main activity.
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    /**
     * ImageView for the company logo
     */
    private ImageView logo;
    /**
     * ImageView for the bank logo
     */
    private ImageView logo_bank;

    /**
     * Creates and initializes the activity.
     *
     * @param savedInstanceState the saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logoC_splash);
        logo.setVisibility(View.GONE);

        logo_bank = findViewById(R.id.logoB_splash);
        logo_bank.setVisibility(View.GONE);

        new Handler().postDelayed(() -> {
            Animation fadeInAll = new AlphaAnimation(0, 1); // Animation to fade in both logos
            Animation fadeInBank = new AlphaAnimation(0, 1); // Animation to fade in bank logo

            fadeInAll.setInterpolator(new AccelerateInterpolator());
            fadeInBank.setInterpolator(new AccelerateInterpolator());

            fadeInAll.setStartOffset(900); // Delay before fading in logos
            fadeInAll.setDuration(800); // Duration of fade animation
            fadeInBank.setDuration(800); // Duration of bank logo fade animation

            logo_bank.setVisibility(View.VISIBLE);
            logo.setVisibility(View.VISIBLE);
            logo_bank.startAnimation(fadeInBank);
            logo.startAnimation(fadeInAll);
        }, 1100);

        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(i);
        }, 3200); // Delay before redirecting to the main activity
    }
}
