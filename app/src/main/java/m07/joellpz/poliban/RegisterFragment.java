package m07.joellpz.poliban;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private EditText emailEditText, passwordEditText, nameEditText, phoneEditText, directionEditText, cpEditText;
    NavController navController;
    private Button registerButton, cancelButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FlexboxLayout registerForm;
    ArchedImageProgressBar polibanArcProgress;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);  // <-----------------

        registerForm = view.findViewById(R.id.registerForm);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        nameEditText = view.findViewById(R.id.nameEditText);
        cpEditText = view.findViewById(R.id.cpEditText);
        directionEditText = view.findViewById(R.id.directionEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        polibanArcProgress = view.findViewById(R.id.custom_imageprogressBar);
        new ChargingImage(polibanArcProgress,this);

        polibanArcProgress.setVisibility(View.GONE);
        registerButton = view.findViewById(R.id.registerButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta();
            }

        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }

        });
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    //TODO profile Image
    private void crearCuenta() {
        if (!validarFormulario()) {
            return;
        }

        registerButton.setEnabled(false);
        registerForm.setVisibility(View.GONE);

        polibanArcProgress.setVisibility(View.VISIBLE);

        try {

            mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                actualizarUI(user);
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("id", user.getUid());
                                userData.put("profilePhoto", null);
                                userData.put("profileName", nameEditText.getText().toString());
                                userData.put("profilePhone", phoneEditText.getText().toString());
                                userData.put("profileDirection", directionEditText.getText().toString());
                                userData.put("profileCP", cpEditText.getText().toString());
                                mFirestore.collection("users").document(user.getUid()).set(userData);
                            } else {
                                Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();
                                System.out.println(task.getException());
                            }
                            polibanArcProgress.setVisibility(View.GONE);
                            registerForm.setVisibility(View.VISIBLE);
                            registerButton.setEnabled(true);
                        }
                    });
        } catch (Exception e) {
            System.out.println(e);
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
}