package m07.joellpz.poliban.adapter.viewHolders;

import android.text.TextUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import m07.joellpz.poliban.databinding.ViewholderBankAccountRegisterBinding;
import m07.joellpz.poliban.model.BankAccount;

public class RegisterBankAccountViewHolder extends RecyclerView.ViewHolder {
    private final ViewholderBankAccountRegisterBinding binding;
    private final FirebaseUser user;

    public RegisterBankAccountViewHolder(ViewholderBankAccountRegisterBinding binding, FirebaseUser user) {
        super(binding.getRoot());
        this.user = user;
        this.binding = binding;
    }

    public void bind() {
        binding.acceptButton.setOnClickListener(l -> {
            if (validateForm()) {
                String iban = binding.ibanEditText.getText().toString().replaceAll("\\s+", "");
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < iban.length(); i++) {
                    if (i > 0 && i % 4 == 0) {
                        result.append(" ");
                    }
                    result.append(iban.charAt(i));
                }


                BankAccount account = new BankAccount(user.getUid(), result.toString(), binding.ownerEditText.getText().toString());
                BankAccount.saveBankAccountToUser(account,(isSaved -> {
                    if (isSaved) {
                        // La cuenta se guardó correctamente
                        binding.ibanEditText.setError(null);
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
        String regex = "^ES\\d{22}$";
        String iban = binding.ibanEditText.getText().toString();
        iban = iban.replaceAll("\\s", "").replaceAll("-", "");

        if (TextUtils.isEmpty(iban)) {
            binding.ibanEditText.setError("Required.");
            valid = false;
        } else if (iban.length() != 24 || !iban.matches("[a-zA-Z\\d]+") || !iban.matches(regex)) {
            binding.ibanEditText.setError("Bad format.");
            valid = false;
        } else binding.ibanEditText.setError(null);


        if (TextUtils.isEmpty(binding.ownerEditText.getText().toString())) {
            binding.ownerEditText.setError("Required.");
            valid = false;
        } else binding.ownerEditText.setError(null);

        return valid;
    }
}