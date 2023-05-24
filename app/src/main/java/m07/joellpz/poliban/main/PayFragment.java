package m07.joellpz.poliban.main;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentPayBinding;
import m07.joellpz.poliban.databinding.ViewholderContactBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Contact;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.tools.ChargingImage;

/**
 * A fragment for making payments.
 */
public class PayFragment extends Fragment {
    /**
     * View binding for the fragment.
     */
    private FragmentPayBinding binding;

    /**
     * Type of Transactions.
     */
    private String status;

    public PayFragment() {
        // Required empty public constructor
    }

    /**
     * Called when the fragment is being created.
     *
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          The parent view that the fragment's UI should be attached to
     * @param savedInstanceState The saved instance state bundle
     * @return The View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentPayBinding.inflate(inflater, container, false)).getRoot();
    }

    /**
     * Called after the view has been created.
     *
     * @param view               The created view
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.status = "transfer";

        FirebaseFirestore.getInstance().collection("bankAccount")
                .whereEqualTo("userId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .get()
                .addOnSuccessListener(docSnap -> binding.spinner.setItems(docSnap.toObjects(BankAccount.class)));
        binding.spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<BankAccount>) (view1, position, id, item) ->
                Snackbar.make(view1, "Clicked " + item, Snackbar.LENGTH_LONG).show());

        List<Contact> contacts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            contacts.add(new Contact("Jordi Herna" + i, "+34 123 456 789"));
        }
        binding.layoutRecyclerView.setAdapter(new ContactsAdapter(contacts));
        binding.customImageProgressBar.setVisibility(View.GONE);
        new ChargingImage(binding.customImageProgressBar, this);

        binding.bizumButtonPay.setOnClickListener(l -> definePaymentPane("bizum"));
        binding.transferButtonPay.setOnClickListener(l -> definePaymentPane("transfer"));
        binding.creditButtonPay.setOnClickListener(l -> definePaymentPane("credit"));
        binding.investButtonPay.setOnClickListener(l -> {
            AlertDialog ad = new AlertDialog.Builder(getContext()).create();
            ad.setMessage("Have not been implemented yet...");
            ad.setCancelable(true);
            ad.show();
        });

        binding.sendButton.setOnClickListener(l -> {
            binding.customImageProgressBar.setVisibility(View.VISIBLE);
            Transaction t = new Transaction(
                    status.equals("bizum") ? "Bizum Jordi" : status,
                    false,
                    status.equals("credit") ? Float.parseFloat(binding.thirdEditTextPay.getText().toString()) : Float.parseFloat(binding.thirdEditTextPay.getText().toString()) * -1,
                    (status.equals("credit") ? "Credit: " + binding.secondEditTextPay.getText().toString() : "") + binding.secondEditTextPay.getText().toString(),
                    new Date(),
                    (BankAccount) binding.spinner.getItems().get(binding.spinner.getSelectedIndex()));

            Transaction.makeTransaction(t).addOnSuccessListener(o -> {
                binding.customImageProgressBar.setVisibility(View.GONE);
                binding.sendButton.setText(R.string.accepted);
                binding.sendButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green));

                binding.firstEditText.setText("");
                binding.secondEditTextPay.setText("");
                binding.thirdEditTextPay.setText("");

                new Handler().postDelayed(() -> {
                    binding.sendButton.setText(R.string.send);
                    binding.sendButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.orange_light));
                }, 2000);
            });
        });
    }

    /**
     * Defines the payment pane based on the selected button.
     *
     * @param button The selected button
     */
    private void definePaymentPane(String button) {
        this.status = button;
        binding.firstEditText.setText("");
        binding.secondEditTextPay.setText("");
        binding.thirdEditTextPay.setText("");

        switch (button) {
            case "bizum":
                binding.layoutRecyclerView.setVisibility(View.VISIBLE);
                binding.infotextView.setText(R.string.money_you_need);

                binding.firstEditText.setVisibility(View.GONE);
                binding.secondEditTextPay.setHint("Subject*");

                binding.sendButton.setText(R.string.send);
                break;

            case "credit":
                binding.layoutRecyclerView.setVisibility(View.GONE);
                binding.infotextView.setText(R.string.money_you_need);

                binding.firstEditText.setVisibility(View.VISIBLE);
                binding.firstEditText.setHint("Months to be returned*");
                binding.firstEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                binding.secondEditTextPay.setHint("Reason*");

                binding.sendButton.setText(R.string.request);
                break;

            case "transfer":
                binding.layoutRecyclerView.setVisibility(View.GONE);
                binding.infotextView.setText(R.string.make_a_transfer_in_the_easiest_way);

                binding.firstEditText.setVisibility(View.VISIBLE);
                binding.firstEditText.setHint("IBAN*");
                binding.firstEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.secondEditTextPay.setHint("Reason*");

                binding.sendButton.setText(R.string.send);
                break;
        }
    }

    /**
     * Adapter class for the Contacts RecyclerView.
     */
    class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
        /**
         * List of Contacts
         */
        private final List<Contact> contactList;

        /**
         * Constructor for the ContactsAdapter.
         *
         * @param contacts The list of contacts to be displayed
         */
        public ContactsAdapter(List<Contact> contacts) {
            contactList = contacts;
        }

        /**
         * Called when a new ViewHolder object should be created.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position
         * @param viewType The view type of the new View
         * @return A new ContactViewHolder that holds the View for each contact item
         */
        @NonNull
        @Override
        public ContactsAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContactsAdapter.ContactViewHolder(ViewholderContactBinding.inflate(getLayoutInflater(), parent, false));
        }

        /**
         * Called to display the data at the specified position.
         *
         * @param holder   The ContactViewHolder that should be updated to represent the contents of the item at the given position
         * @param position The position of the item within the adapter's data set
         */
        @Override
        public void onBindViewHolder(@NonNull ContactsAdapter.ContactViewHolder holder, int position) {
            if (position % 2 == 0) {
                holder.bindingContact.mainLayout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue_light)));
            }

            holder.bindingContact.mainLayout.setOnClickListener(l -> {
                if (holder.bindingContact.tick.getVisibility() == View.VISIBLE) {
                    holder.bindingContact.tick.setVisibility(View.INVISIBLE);
                } else {
                    holder.bindingContact.tick.setVisibility(View.VISIBLE);
                }
            });
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in the adapter
         */
        @Override
        public int getItemCount() {
            return contactList.size();
        }

        /**
         * ViewHolder class for the Contact item view.
         */
        class ContactViewHolder extends RecyclerView.ViewHolder {
            private final ViewholderContactBinding bindingContact;

            /**
             * Constructor for the ContactViewHolder.
             *
             * @param binding The view binding for the contact item
             */
            public ContactViewHolder(ViewholderContactBinding binding) {
                super(binding.getRoot());
                this.bindingContact = binding;
            }
        }
    }
}