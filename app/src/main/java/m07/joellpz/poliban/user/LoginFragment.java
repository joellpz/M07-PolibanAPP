package m07.joellpz.poliban.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentLoginBinding;
import m07.joellpz.poliban.tools.ChargingImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment #newInstance} factory method to create an instance of this fragment.
 * This fragment handles user login functionality.
 */
public class LoginFragment extends Fragment {

    /**
     * Navigation controller for navigating between fragments.
     */
    private NavController navController;
    /**
     * View binding for the fragment.
     */
    private FragmentLoginBinding binding;
    /**
     * Firebase Authenticator.
     */
    private FirebaseAuth mAuth;

    /**
     * Default constructor for LoginFragment.
     */
    public LoginFragment() {
    }

    /**
     * Inflates the fragment view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The inflated view for the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentLoginBinding.inflate(inflater, container, false)).getRoot();
    }

    /**
     * Called when the view creation is complete.
     *
     * @param view               The created view
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        binding.toRegisterFragment.setOnClickListener(view1 -> navController.navigate(R.id.registerFragment));

        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.GONE);

        binding.loginButton.setOnClickListener(view1 -> {
            if (validarFormulario()) accederConEmail();
        });

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Logs in the user with email and password.
     */
    private void accederConEmail() {
        binding.signInForm.setVisibility(View.GONE);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(binding.emailEditText.getText().toString(), binding.passwordEditText.getText().toString())
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        actualizarUI(mAuth.getCurrentUser());
                    } else {
                        Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                    }
                    binding.signInForm.setVisibility(View.VISIBLE);
                    binding.customImageProgressBar.setVisibility(View.GONE);
                });
    }

    /**
     * Updates the UI based on the current user.
     *
     * @param currentUser The current user
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

        if (TextUtils.isEmpty(binding.emailEditText.getText().toString())) {
            binding.emailEditText.setError("Required.");
            valid = false;
        } else {
            binding.emailEditText.setError(null);
        }

        if (TextUtils.isEmpty(binding.passwordEditText.getText().toString())) {
            binding.passwordEditText.setError("Required.");
            valid = false;
        } else {
            binding.passwordEditText.setError(null);
        }

        return valid;
    }

}
