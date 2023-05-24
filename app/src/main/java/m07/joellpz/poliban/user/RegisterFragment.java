package m07.joellpz.poliban.user;

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

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import java.util.Objects;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.model.AppViewModel;
import m07.joellpz.poliban.model.User;
import m07.joellpz.poliban.tools.ChargingImage;
import m07.joellpz.poliban.tools.UpdateProfileImage;

/**
 * A fragment for user registration.
 */
public class RegisterFragment extends Fragment {

    /**
     * EditText for entering the email.
     */
    private EditText emailEditText;

    /**
     * EditText for entering the password.
     */
    private EditText passwordEditText;

    /**
     * EditText for entering the name.
     */
    private EditText nameEditText;

    /**
     * EditText for entering the phone number.
     */
    private EditText phoneEditText;

    /**
     * EditText for entering the address.
     */
    private EditText directionEditText;

    /**
     * EditText for entering the postal code.
     */
    private EditText cpEditText;

    /**
     * Navigation controller for navigating between fragments.
     */
    private NavController navController;

    /**
     * Button for registration.
     */
    private Button registerButton;

    /**
     * Firebase authentication instance.
     */
    private FirebaseAuth mAuth;

    /**
     * Firebase Firestore instance.
     */
    private FirebaseFirestore mFirestore;

    /**
     * Form layout for registration.
     */
    private FlexboxLayout registerForm;

    /**
     * Progress bar for image loading.
     */
    private ArchedImageProgressBar polibanArcProgress;

    /**
     * Profile photo URL.
     */
    private Uri photoURL;

    /**
     * ViewModel for the app.
     */
    public AppViewModel appViewModel;

    /**
     * Default constructor for RegisterFragment.
     */
    public RegisterFragment() {
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The inflated view hierarchy of the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view.
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the AppViewModel from the ViewModelProvider
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        // Get the NavigationController associated with the fragment's view
        navController = Navigation.findNavController(view);

        // Initialize UI elements
        registerForm = view.findViewById(R.id.registerForm);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        cpEditText = view.findViewById(R.id.cpEditText);
        directionEditText = view.findViewById(R.id.directionEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        polibanArcProgress = view.findViewById(R.id.custom_imageProgressBar);

        // Set up image loading progress bar
        new ChargingImage(polibanArcProgress, this);

        polibanArcProgress.setVisibility(View.GONE);
        registerButton = view.findViewById(R.id.registerButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // Set click listeners for register and cancel buttons
        registerButton.setOnClickListener(view1 -> crearCuenta());
        cancelButton.setOnClickListener(view1 -> navController.popBackStack());

        // Get instances of FirebaseAuth and FirebaseFirestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        // Set click listener for the profile image to launch the gallery
        view.findViewById(R.id.profileImg).setOnClickListener(v -> galeria.launch("image/*"));

        // Observe the selected media from the AppViewModel and update the profile image accordingly
        appViewModel.mediaSeleccionado.observe(getViewLifecycleOwner(), media -> {
            if (media.uri != null) {
                Glide.with(requireContext()).load(media.uri).circleCrop().into((ImageView) view.findViewById(R.id.profileImg));
                photoURL = media.uri;
            }
        });
    }

    /**
     * Creates a user account using the provided registration data.
     * Performs form validation, authentication, and saves user data to Firestore.
     */
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
                            User userData = new User();
                            userData.setUid(Objects.requireNonNull(user).getUid());
                            if (photoURL != null) {
                                UpdateProfileImage.pujaGuardarEnFirestore(photoURL, user);
                            } else {
                                userData.setProfilePhoto(null);
                            }
                            userData.setProfileName(nameEditText.getText().toString());
                            userData.setProfilePhone(phoneEditText.getText().toString());
                            userData.setProfileDirection(directionEditText.getText().toString());
                            userData.setProfileCP(cpEditText.getText().toString());
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

    /**
     * Updates the UI based on the current user after successful authentication.
     *
     * @param currentUser The currently authenticated FirebaseUser.
     */
    private void actualizarUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            navController.navigate(R.id.homeFragment);
        }
    }

    /**
     * Validates the registration form by checking if all required fields are filled.
     *
     * @return True if the form is valid, false otherwise.
     */
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

    /**
     * The ActivityResultLauncher for selecting an image from the gallery.
     */
    protected final ActivityResultLauncher<String> galeria =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> appViewModel.setMediaSeleccionado(uri, "image"));
}
