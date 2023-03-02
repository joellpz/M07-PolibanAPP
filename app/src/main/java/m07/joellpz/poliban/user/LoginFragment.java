package m07.joellpz.poliban.user;

import android.os.Bundle;
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
 * Use the {@link LoginFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    NavController navController;
    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;



    public LoginFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentLoginBinding.inflate(inflater, container, false)).getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        requireActivity().findViewById(R.id.bottomMainMenu).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);

        binding.toRegisterFragment.setOnClickListener(view1 -> navController.navigate(R.id.registerFragment));

        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.GONE);

        binding.loginButton.setOnClickListener(view1 -> accederConEmail());

        mAuth = FirebaseAuth.getInstance();

    }

    private void accederConEmail() {
        binding.signInForm.setVisibility(View.GONE);
        //signInProgressBar.setVisibility(View.VISIBLE);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword("joel@gmail.com", "joel2001")
                //mAuth.signInWithEmailAndPassword(binding.emailEditText.getText().toString(), binding.passwordEditText.getText().toString())
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        actualizarUI(mAuth.getCurrentUser());
                    } else {
                        Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                    }
                    binding.signInForm.setVisibility(View.VISIBLE);
//                    signInProgressBar.setVisibility(View.GONE);
                    binding.customImageProgressBar.setVisibility(View.GONE);
                });
    }

    private void actualizarUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            navController.navigate(R.id.homeFragment);
        }
    }

}