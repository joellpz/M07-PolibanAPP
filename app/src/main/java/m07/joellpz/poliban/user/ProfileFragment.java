package m07.joellpz.poliban.user;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentProfileBinding;
import m07.joellpz.poliban.model.AppViewModel;
import m07.joellpz.poliban.tools.ChargingImage;
import m07.joellpz.poliban.tools.UpdateProfileImage;

/**
 * A fragment for user profile.
 */
public class ProfileFragment extends Fragment {

    /**
     * Navigation controller for navigating between fragments.
     */
    private NavController navController;

    /**
     * View binding for the fragment.
     */
    private FragmentProfileBinding binding;

    /**
     * Toolbar for the fragment.
     */
    private Toolbar toolbar;

    /**
     * Firebase user instance.
     */
    private FirebaseUser user;

    /**
     * Profile photo URL.
     */
    private Uri photoURL;

    /**
     * ViewModel for the app.
     */
    public AppViewModel appViewModel;

    /**
     * Default constructor for ProfileFragment.
     */
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Callback method for when the view has been created.
     *
     * @param view               The created view
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar.setVisibility(View.GONE);
        requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.GONE);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        navController = Navigation.findNavController(view);

        binding.editProfileForm.setVisibility(View.GONE);

        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.updateButtonProfile.setOnClickListener(view1 -> updateProfile());
        binding.cancelButtonProfile.setOnClickListener(view1 -> {
            navController.popBackStack();
            toolbar.setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.VISIBLE);
        });

        binding.profileImgProfile.setOnClickListener(v -> galeria.launch("image/*"));
        appViewModel.mediaSeleccionado.observe(getViewLifecycleOwner(), media -> {
            if (media.uri != null) {
                Glide.with(requireContext()).load(media.uri).circleCrop().into(binding.profileImgProfile);
                photoURL = media.uri;
            }
        });

        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot docSnap = task.getResult();
                        if (docSnap.exists()) {
                            binding.emailEditTextProfile.setText(user.getEmail());
                            binding.nameEditTextProfile.setText(Objects.requireNonNull(docSnap.get("profileName")).toString());
                            binding.phoneEditTextProfile.setText(Objects.requireNonNull(docSnap.get("profilePhone")).toString());
                            binding.directionEditTextProfile.setText(Objects.requireNonNull(docSnap.get("profileDirection")).toString());
                            binding.cpEditTextProfile.setText(Objects.requireNonNull(docSnap.get("profileCP")).toString());
                            if (docSnap.get("profilePhoto") != null)
                                Glide.with(requireContext()).load(Objects.requireNonNull(docSnap.get("profilePhoto")).toString()).circleCrop().into(binding.profileImgProfile);
                            else
                                Glide.with(requireContext()).load(R.drawable.profile_img).circleCrop().into(binding.profileImgProfile);
                            System.out.println("ENCONTRADO");
                        } else {
                            System.out.println("Documento no encontrado");
                        }
                    } else {
                        System.out.println(Objects.requireNonNull(task.getException()).getMessage());
                    }
                    binding.editProfileForm.setVisibility(View.VISIBLE);
                    binding.customImageProgressBar.setVisibility(View.GONE);
                });
    }

    /**
     * Updates the user profile.
     */
    private void updateProfile() {
        if (!validarFormulario()) {
            return;
        }
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        if (photoURL != null)
            UpdateProfileImage.pujaGuardarEnFirestore(photoURL, user);
        Map<String, Object> userData = new HashMap<>();
        userData.put("profileName", binding.nameEditTextProfile.getText().toString());
        userData.put("profilePhone", binding.phoneEditTextProfile.getText().toString());
        userData.put("profileDirection", binding.directionEditTextProfile.getText().toString());
        userData.put("profileCP", binding.cpEditTextProfile.getText().toString());
        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).update(userData).addOnSuccessListener(l -> {
            binding.customImageProgressBar.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.VISIBLE);
            navController.popBackStack();
        });
    }

    /**
     * Called when the fragment is being created.
     *
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = requireActivity().findViewById(R.id.toolbar);
    }

    /**
     * Called to create the view for the fragment.
     *
     * @param inflater           The layout inflater
     * @param container          The view group container
     * @param savedInstanceState The saved instance state
     * @return The created view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentProfileBinding.inflate(inflater, container, false)).getRoot();
    }

    /**
     * Validates the profile form.
     *
     * @return true if the form is valid, false otherwise
     */
    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(binding.nameEditTextProfile.getText().toString())) {
            binding.nameEditTextProfile.setError("Required.");
            valid = false;
        } else {
            binding.nameEditTextProfile.setError(null);
        }

        if (TextUtils.isEmpty(binding.phoneEditTextProfile.getText().toString())) {
            binding.phoneEditTextProfile.setError("Required.");
            valid = false;
        } else {
            binding.phoneEditTextProfile.setError(null);
        }

        if (TextUtils.isEmpty(binding.directionEditTextProfile.getText().toString())) {
            binding.directionEditTextProfile.setError("Required.");
            valid = false;
        } else {
            binding.directionEditTextProfile.setError(null);
        }

        if (TextUtils.isEmpty(binding.cpEditTextProfile.getText().toString())) {
            binding.cpEditTextProfile.setError("Required.");
            valid = false;
        } else {
            binding.cpEditTextProfile.setError(null);
        }

        return valid;
    }

    /**
     * Activity result launcher for selecting an image from the gallery.
     */
    protected final ActivityResultLauncher<String> galeria =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> appViewModel.setMediaSeleccionado(uri, "image"));
}
