package m07.joellpz.poliban;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import java.util.Objects;

import m07.joellpz.poliban.tools.ChargingImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private NavController navController;

    private ArchedImageProgressBar polibanArcProgress;

    private FirebaseUser user;
    private Toolbar toolbar;
    private ScrollView mainView;
    private Uri photoURL;

    public HomeFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        mainView = view.findViewById(R.id.mainView);
        mainView.setVisibility(View.GONE);
        polibanArcProgress = view.findViewById(R.id.custom_imageProgressBar);
        new ChargingImage(polibanArcProgress,this);
        toolbar.findViewById(R.id.profileAppBarImage).setOnClickListener(l -> navController.navigate(R.id.profileFragment));

        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .get().addOnSuccessListener(docSnap -> {
                    if (docSnap.exists()) {
                        if (docSnap.get("profilePhoto") != null)
                            Glide.with(requireContext()).load(docSnap.get("profilePhoto")).circleCrop().into((ImageView) toolbar.findViewById(R.id.profileAppBarImage));
                        else
                            Glide.with(requireContext()).load(R.drawable.profile_img).circleCrop().into((ImageView) toolbar.findViewById(R.id.profileAppBarImage));
                    }
                    toolbar.setVisibility(View.VISIBLE);
                    mainView.setVisibility(View.VISIBLE);
                    polibanArcProgress.setVisibility(View.GONE);
                });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO IMPORATNTE ESTO DE CAMBIAR LA TOOLBAR
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}