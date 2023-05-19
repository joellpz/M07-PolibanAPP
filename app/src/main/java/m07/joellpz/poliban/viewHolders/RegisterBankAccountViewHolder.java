package m07.joellpz.poliban.viewHolders;

import android.text.TextUtils;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.ViewholderRegisterBankAccountBinding;
import m07.joellpz.poliban.main.HomeFragment;
import m07.joellpz.poliban.model.BankAccount;

public class RegisterBankAccountViewHolder extends RecyclerView.ViewHolder {
    private final ViewholderRegisterBankAccountBinding binding;
    private final Fragment parentFragment;
    private final FirebaseUser user;

    public RegisterBankAccountViewHolder(ViewholderRegisterBankAccountBinding binding, Fragment parentFragment, FirebaseUser user) {
        super(binding.getRoot());
        this.parentFragment = parentFragment;
        this.user = user;
        this.binding = binding;
    }

    public void bind() {
        binding.acceptButton.setOnClickListener(l -> {
            if (validateForm()) {
                BankAccount account = new BankAccount(user.getUid(), binding.ibanEditText.getText().toString(), binding.ownerEditText.getText().toString());
                account.saveBankAccountToUser((isSaved -> {
                    if (isSaved) {
                        // La cuenta se guardó correctamente
                        binding.ibanEditText.setError(null);
                        //TODO MIRAR ESTO para que haga redireccion al nuevo
                        //TODO Tambien mirar como marcar que una List<Transacionts> sea una nueva coleccion dentro de un documento.
                        RecyclerView recyclerView = parentFragment.getView().findViewById(R.id.recyclerViewHome);
                        recyclerView.scrollToPosition(2);
                    } else {
                        // La cuenta ya existe o hubo un error al guardar
                        binding.ibanEditText.setError("This IBAN is already registered!");
                    }
                }));
            }
        });
    }

    public boolean validateForm() {
        boolean valid = true;
        if (TextUtils.isEmpty(binding.ibanEditText.getText().toString())) {
            binding.ibanEditText.setError("Required.");
            valid = false;
        } else {
            binding.ibanEditText.setError(null);
        }

        if (TextUtils.isEmpty(binding.ownerEditText.getText().toString())) {
            binding.ownerEditText.setError("Required.");
            valid = false;
        } else {
            binding.ownerEditText.setError(null);
        }

        return valid;
    }
}