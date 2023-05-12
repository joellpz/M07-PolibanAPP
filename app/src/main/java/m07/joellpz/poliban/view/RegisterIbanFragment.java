package m07.joellpz.poliban.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentRegisterIbanBinding;
import m07.joellpz.poliban.main.HomeFragment;
import m07.joellpz.poliban.model.BankAccount;

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
        user = FirebaseAuth.getInstance().getCurrentUser();
        view.findViewById(R.id.acceptButton).setOnClickListener(l -> {
            BankAccount account = new BankAccount(user.getUid(), binding.ibanEditText.getText().toString(), binding.ownerEditText.getText().toString());
            account.saveBankAccountToUser((isSaved -> {
                if (isSaved) {
                    // La cuenta se guardó correctamente
                    binding.ibanEditText.setError(null);
                    requireActivity().recreate();
                } else {
                    // La cuenta ya existe o hubo un error al guardar
                    binding.ibanEditText.setError("This IBAN is already registered!");
                }
            }));
        });
    }

    private void recargarTabs() {
        // Obtén una referencia al Fragment que contiene el TabbedLayout
        Fragment tabbedFragment = getChildFragmentManager().findFragmentById(R.id.tabLayout);

        if (tabbedFragment instanceof HomeFragment) {
            // Llama a un método personalizado en el Fragment que contiene el TabbedLayout
            ((HomeFragment) tabbedFragment).recargarTabbedLayout();
        }
    }
}