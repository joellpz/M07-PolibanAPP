package m07.joellpz.poliban.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DecimalFormat;
import java.util.Objects;

import m07.joellpz.poliban.adapter.BankAccountAdapter;
import m07.joellpz.poliban.databinding.FragmentWalletBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.tools.ChargingImage;

public class WalletFragment extends Fragment {

    private FragmentWalletBinding binding;
    DecimalFormat df = new DecimalFormat("#.##");

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentWalletBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        super.onViewCreated(view, savedInstanceState);

        binding.mainView.setVisibility(View.GONE);

        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        Query q = FirebaseFirestore.getInstance().collection("bankAccount").whereEqualTo("userId", Objects.requireNonNull(user).getUid());
        FirestoreRecyclerOptions<BankAccount> options = new FirestoreRecyclerOptions.Builder<BankAccount>()
                .setQuery(q, BankAccount.class)
                .setLifecycleOwner(this)
                .build();
        binding.walletRecyclerView.setAdapter(new BankAccountAdapter(options, this, user, true));

        binding.mainView.setVisibility(View.VISIBLE);
        binding.customImageProgressBar.setVisibility(View.GONE);
    }
}