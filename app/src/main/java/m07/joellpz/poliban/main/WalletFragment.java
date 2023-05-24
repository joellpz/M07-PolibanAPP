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

import java.util.Objects;

import m07.joellpz.poliban.adapter.BankAccountAdapter;
import m07.joellpz.poliban.databinding.FragmentWalletBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.tools.ChargingImage;

/**
 * A fragment that displays the user's wallet and bank accounts.
 */
public class WalletFragment extends Fragment {

    /**
     * View binding for the fragment.
     */
    private FragmentWalletBinding binding;

    /**
     * Default constructor for WalletFragment.
     */
    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Called to do initial creation of the fragment.
     *
     * @param savedInstanceState The saved instance state Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the onCreate method of the base class
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater used to inflate the layout.
     * @param container          The parent ViewGroup.
     * @param savedInstanceState The saved instance state Bundle.
     * @return The View for the fragment's UI, or null.
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentWalletBinding.inflate(inflater, container, false)).getRoot();
    }

    /**
     * Called when the view creation is complete.
     *
     * @param view               The created view
     * @param savedInstanceState The saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Get the currently logged-in user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        super.onViewCreated(view, savedInstanceState);

        binding.mainView.setVisibility(View.GONE);

        // Set up the charging image progress bar
        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        // Query the bank accounts for the current user
        Query q = FirebaseFirestore.getInstance().collection("bankAccount").whereEqualTo("userId", Objects.requireNonNull(user).getUid());
        FirestoreRecyclerOptions<BankAccount> options = new FirestoreRecyclerOptions.Builder<BankAccount>()
                .setQuery(q, BankAccount.class)
                .setLifecycleOwner(this)
                .build();
        // Set the bank account adapter for the recycler view
        binding.walletRecyclerView.setAdapter(new BankAccountAdapter(options, this, user, true));

        binding.mainView.setVisibility(View.VISIBLE);
        binding.customImageProgressBar.setVisibility(View.GONE);
    }
}
