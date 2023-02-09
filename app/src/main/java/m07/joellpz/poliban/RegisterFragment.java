package m07.joellpz.poliban;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.bumptech.glide.Glide;

import m07.joellpz.poliban.tools.UpdateProfileImage;
import m07.joellpz.poliban.view.AppViewModel;
import m07.joellpz.poliban.view.ChargingImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private EditText emailEditText, passwordEditText, nameEditText, phoneEditText, directionEditText, cpEditText;
    NavController navController;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FlexboxLayout registerForm;
    ArchedImageProgressBar polibanArcProgress;

    private Uri photoURL;
    public AppViewModel appViewModel;


    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        navController = Navigation.findNavController(view);  // <-----------------

        registerForm = view.findViewById(R.id.registerForm);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        cpEditText = view.findViewById(R.id.cpEditText);
        directionEditText = view.findViewById(R.id.directionEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        polibanArcProgress = view.findViewById(R.id.custom_imageProgressBar);
        new ChargingImage(polibanArcProgress, this);

        polibanArcProgress.setVisibility(View.GONE);
        registerButton = view.findViewById(R.id.registerButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        registerButton.setOnClickListener(view1 -> crearCuenta());
        cancelButton.setOnClickListener(view1 -> navController.popBackStack());
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        view.findViewById(R.id.profileImg).setOnClickListener(v -> galeria.launch("image/*"));
        appViewModel.mediaSeleccionado.observe(getViewLifecycleOwner(), media -> {
            if (media.uri != null) {
                Glide.with(requireContext()).load(media.uri).circleCrop().into((ImageView) view.findViewById(R.id.profileImg));
                photoURL = media.uri;
            }
        });
    }

    private void crearCuenta() {
        if (!validarFormulario()) {
            return;
        }

        registerButton.setEnabled(false);
        registerForm.setVisibility(View.GONE);

        polibanArcProgress.setVisibility(View.VISIBLE);


        try {
            mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(requireActivity(), task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            actualizarUI(user);
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("id", Objects.requireNonNull(user).getUid());
                            if (photoURL != null) {
                                UpdateProfileImage.pujaIguardarEnFirestore(photoURL,user);
                            } else userData.put("profilePhoto", "R.drawable.profile_img");
                            userData.put("profileName", nameEditText.getText().toString());
                            userData.put("profilePhone", phoneEditText.getText().toString());
                            userData.put("profileDirection", directionEditText.getText().toString());
                            userData.put("profileCP", cpEditText.getText().toString());
                            mFirestore.collection("users").document(user.getUid()).set(userData);
                        } else {
                            Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                        }
                        polibanArcProgress.setVisibility(View.GONE);
                        registerForm.setVisibility(View.VISIBLE);
                        registerButton.setEnabled(true);
                    });
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    private void actualizarUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            navController.navigate(R.id.homeFragment);
        }
    }

    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            nameEditText.setError("Required.");
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        if (TextUtils.isEmpty(passwordEditText.getText().toString())) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
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