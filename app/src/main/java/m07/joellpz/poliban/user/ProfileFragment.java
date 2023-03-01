package m07.joellpz.poliban.user;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.tools.ChargingImage;
import m07.joellpz.poliban.tools.UpdateProfileImage;
import m07.joellpz.poliban.view.AppViewModel;

public class ProfileFragment extends Fragment {

    private NavController navController;
    private Toolbar toolbar;
    private ArchedImageProgressBar polibanArcProgress;
    private FlexboxLayout editProfileForm;
    private FirebaseUser user;
    private Uri photoURL;
    public AppViewModel appViewModel;
    private EditText emailEditText, nameEditText, phoneEditText, directionEditText, cpEditText;



    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setVisibility(View.GONE);
        requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.GONE);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        navController = Navigation.findNavController(view);
        editProfileForm = view.findViewById(R.id.editProfileForm);
        editProfileForm.setVisibility(View.GONE);
        polibanArcProgress = view.findViewById(R.id.custom_imageProgressBar);


        emailEditText = view.findViewById(R.id.emailEditTextProfile);
        nameEditText = view.findViewById(R.id.nameEditTextProfile);
        cpEditText = view.findViewById(R.id.cpEditTextProfile);
        directionEditText = view.findViewById(R.id.directionEditTextProfile);
        phoneEditText = view.findViewById(R.id.phoneEditTextProfile);

        new ChargingImage(polibanArcProgress, this);
        polibanArcProgress.setVisibility(View.VISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();

        view.findViewById(R.id.updateButtonProfile).setOnClickListener(view1 -> updateProfile());
        view.findViewById(R.id.cancelButtonProfile).setOnClickListener(view1 -> {
            navController.popBackStack();
            toolbar.setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.VISIBLE);
        });


        view.findViewById(R.id.profileImgProfile).setOnClickListener(v -> galeria.launch("image/*"));
        appViewModel.mediaSeleccionado.observe(getViewLifecycleOwner(), media -> {
            if (media.uri != null) {
                Glide.with(requireContext()).load(media.uri).circleCrop().into((ImageView) view.findViewById(R.id.profileImgProfile));
                photoURL = media.uri;
            }
        });

        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot docSnap = task.getResult();
                        if (docSnap.exists()) {
                            emailEditText.setText(user.getEmail());
                            nameEditText.setText(Objects.requireNonNull(docSnap.get("profileName")).toString());
                            phoneEditText.setText(Objects.requireNonNull(docSnap.get("profilePhone")).toString());
                            directionEditText.setText(Objects.requireNonNull(docSnap.get("profileDirection")).toString());
                            cpEditText.setText(Objects.requireNonNull(docSnap.get("profileCP")).toString());
                            Glide.with(requireContext()).load(Objects.requireNonNull(docSnap.get("profilePhoto")).toString()).circleCrop().into((ImageView) view.findViewById(R.id.profileImgProfile));
                            System.out.println("ENCOTRADO");
                        } else {
                            System.out.println("Documento no encontrado");
                        }
                    } else {
                        System.out.println(Objects.requireNonNull(task.getException()).getMessage());
                    }
                    editProfileForm.setVisibility(View.VISIBLE);
                    polibanArcProgress.setVisibility(View.GONE);
                });
        //Map<String, Object> userData = UpdateProfileImage.getUserData(user);
//        Glide.with(requireContext()).load(userData.get("profilePhoto")).circleCrop().into((ImageView) view.findViewById(R.id.profileImgProfile));
//        emailEditText.setText(user.getEmail());
//        nameEditText.setText((CharSequence) userData.get("profileName"));
//        cpEditText.setText((CharSequence) userData.get("profileCP"));
//        directionEditText.setText((CharSequence) userData.get("profileDirection"));
//        phoneEditText.setText((CharSequence) userData.get("profilePhone"));
    }

    private void updateProfile() {
        if (!validarFormulario()) {
            return;
        }
        polibanArcProgress.setVisibility(View.VISIBLE);

        if (photoURL != null)
            UpdateProfileImage.pujaIguardarEnFirestore(photoURL, user);
        Map<String, Object> userData = new HashMap<>();
        userData.put("profileName", nameEditText.getText().toString());
        userData.put("profilePhone", phoneEditText.getText().toString());
        userData.put("profileDirection", directionEditText.getText().toString());
        userData.put("profileCP", cpEditText.getText().toString());
        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).update(userData).addOnSuccessListener(l -> {
            polibanArcProgress.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.VISIBLE);
            navController.popBackStack();
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private boolean validarFormulario() {
        boolean valid = true;


        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            nameEditText.setError("Required.");
            valid = false;
        } else {
            nameEditText.setError(null);
        }


        if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            phoneEditText.setError("Required.");
            valid = false;
        } else {
            phoneEditText.setError(null);
        }

        if (TextUtils.isEmpty(directionEditText.getText().toString())) {
            directionEditText.setError("Required.");
            valid = false;
        } else {
            directionEditText.setError(null);
        }

        if (TextUtils.isEmpty(cpEditText.getText().toString())) {
            cpEditText.setError("Required.");
            valid = false;
        } else {
            cpEditText.setError(null);
        }

        return valid;

    }

    protected final ActivityResultLauncher<String> galeria =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> appViewModel.setMediaSeleccionado(uri, "image"));
}