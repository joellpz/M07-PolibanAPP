package m07.joellpz.poliban.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.fragment.app.Fragment;

import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import m07.joellpz.poliban.R;

/**
 * Helper class for configuring and customizing an ArchedImageProgressBar.
 */
public class ChargingImage {
    private final ArchedImageProgressBar polibanArcProgress;

    /**
     * Constructor for ChargingImage.
     *
     * @param polibanArcProgress The ArchedImageProgressBar to be configured.
     * @param fragment           The Fragment associated with the ArchedImageProgressBar.
     */
    public ChargingImage(ArchedImageProgressBar polibanArcProgress, Fragment fragment) {
        this.polibanArcProgress = polibanArcProgress;
        setProperties(fragment);
    }

    /**
     * Sets the properties and customizations for the ArchedImageProgressBar.
     *
     * @param fragment The Fragment associated with the ArchedImageProgressBar.
     */
    private void setProperties(Fragment fragment) {
        Bitmap polibanIcon = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.icon_poliban);
        polibanArcProgress.setProgressImage(polibanIcon, 20.0f);
        polibanArcProgress.setCircleSize(35.0f);
        polibanArcProgress.setCircleColor(Color.parseColor("#5C7C9D"));
        polibanArcProgress.setArchSize(40.0f);
        polibanArcProgress.setArchColor(Color.parseColor("#FF9966"));
        polibanArcProgress.setArchLength(150);
        polibanArcProgress.setArchStroke(8.85f);
        polibanArcProgress.setArchSpeed(5);
    }
}
