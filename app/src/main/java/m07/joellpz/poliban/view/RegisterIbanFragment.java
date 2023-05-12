package m07.joellpz.poliban.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentIbanMainBinding;
import m07.joellpz.poliban.databinding.FragmentRegisterIbanBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterIbanFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterIbanFragment extends Fragment {

    private FirebaseUser user;
    private FragmentRegisterIbanBinding binding;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public RegisterIbanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentRegisterIbanBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.acceptButton).setOnClickListener(l -> requireActivity().recreate());
    }

    public void saveBankAccount() {
        FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid()).get().addOnSuccessListener(docSnap -> {
                    HashMap<String,Boolean> accounts = (HashMap<String,Boolean>) docSnap.get("bankAccounts");
                    if (accounts.containsKey(binding.ibanEditText.toString())){

                    }
                });
    }
}