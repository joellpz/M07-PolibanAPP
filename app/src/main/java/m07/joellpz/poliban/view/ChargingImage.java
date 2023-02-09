package m07.joellpz.poliban.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.fragment.app.Fragment;

import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import m07.joellpz.poliban.R;

public class ChargingImage {
    ArchedImageProgressBar polibanArcProgress;

    public ChargingImage(ArchedImageProgressBar polibanArcProgress, Fragment fragment) {
        this.polibanArcProgress = polibanArcProgress;
        setPropieties(fragment);
    }

    private void setPropieties(Fragment fragment){
        Bitmap polibanIcon = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.icon_poliban);
        polibanArcProgress.setProgressImage(polibanIcon,20.0f);
        polibanArcProgress.setCircleSize(35.0f);
        polibanArcProgress.setCircleColor(Color.parseColor("#5C7C9D"));
        polibanArcProgress.setArchSize(40.0f);
        polibanArcProgress.setArchColor(Color.parseColor("#FF9966"));
        polibanArcProgress.setArchLength(150);
        polibanArcProgress.setArchStroke(8.85f);
        polibanArcProgress.setArchSpeed(5);
    }
}
