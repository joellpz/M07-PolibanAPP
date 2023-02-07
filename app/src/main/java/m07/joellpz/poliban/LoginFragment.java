package m07.joellpz.poliban;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import m07.joellpz.poliban.databinding.ActivityMainBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    NavController navController;

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private LinearLayout signInForm;
    private ProgressBar signInProgressBar;

    private FirebaseAuth mAuth;

    private ActivityMainBinding binding;


    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        view.findViewById(R.id.toRegisterFragment).setOnClickListener(view1 -> navController.navigate(R.id.registerFragment));

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginButton= view.findViewById(R.id.loginButton);
        signInForm = view.findViewById(R.id.signInForm);
        signInProgressBar = view.findViewById(R.id.signInProgressBar);

        loginButton.setOnClickListener(view12 -> accederConEmail());

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
    }

    private void accederConEmail() {
        signInForm.setVisibility(View.GONE);
        signInProgressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        actualizarUI(mAuth.getCurrentUser());
                    } else {
                        Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                        System.out.println(task.getException()+"*************************************************");
                    }
                    signInForm.setVisibility(View.VISIBLE);
                    signInProgressBar.setVisibility(View.GONE);
                });
    }

    private void actualizarUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            navController.navigate(R.id.homeFragment);
        }
    }

}