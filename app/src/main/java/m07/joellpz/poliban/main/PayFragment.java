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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentPayBinding;
import m07.joellpz.poliban.databinding.ViewholderContactBinding;
import m07.joellpz.poliban.model.Contact;
import m07.joellpz.poliban.tools.ChargingImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FragmentPayBinding binding;
    public PayFragment() {
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
        return (binding = FragmentPayBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println(getParentFragment());
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
        binding.investButtonPay.setOnClickListener(l ->{
                    AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                    ad.setMessage("Have not been implemented yet...");
                    ad.setCancelable(true);
                    ad.show();
                });

        binding.sendButton.setOnClickListener(l -> {
            binding.customImageProgressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                binding.customImageProgressBar.setVisibility(View.GONE);
                binding.sendButton.setText("ACCEPTED");
                binding.sendButton.setBackgroundColor(getResources().getColor(R.color.green));
            }, 2000);
            new Handler().postDelayed(() -> {
                binding.sendButton.setText("SEND");
                binding.sendButton.setBackgroundColor(getResources().getColor(R.color.orange_light));
            }, 4000);
        });
        //binding.investButtonPay.setOnClickListener(l ->);
    }

    private void definePaymentPane(String button) {
        switch (button) {
            case "bizum":
                binding.layoutRecyclerView.setVisibility(View.VISIBLE);
                binding.infotextView.setText("How many money do you need?");

                binding.firstEditText.setVisibility(View.GONE);
                binding.secondEditTextPay.setHint("Subject*");

                binding.sendButton.setText("SEND");
                break;

            case "credit":
                binding.layoutRecyclerView.setVisibility(View.GONE);
                binding.infotextView.setText("How many money do you need?");

                binding.firstEditText.setVisibility(View.VISIBLE);
                binding.firstEditText.setHint("Months to be returned*");
                binding.firstEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                binding.secondEditTextPay.setHint("Reason*");

                binding.sendButton.setText("REQUEST");
                break;

            case "transfer":
                binding.layoutRecyclerView.setVisibility(View.GONE);
                binding.infotextView.setText("Make a transfer in the easiest way!");

                binding.firstEditText.setVisibility(View.VISIBLE);
                binding.firstEditText.setHint("IBAN*");
                binding.firstEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.secondEditTextPay.setHint("Reason*");

                binding.sendButton.setText("SEND");
                break;
        }
    }

    class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
        private List<Contact> mExampleList;

        public ContactsAdapter(List<Contact> exampleList) {
            mExampleList = exampleList;
        }

        @NonNull
        @Override
        public ContactsAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContactsAdapter.ContactViewHolder(ViewholderContactBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContactsAdapter.ContactViewHolder holder, int position) {
            Contact currentItem = mExampleList.get(position);
            if (position % 2 == 0) {
                holder.bindingContact.mainLayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_light)));
            }

            holder.bindingContact.mainLayout.setOnClickListener(l -> {
                if (holder.bindingContact.tick.getVisibility() == View.VISIBLE)
                    holder.bindingContact.tick.setVisibility(View.INVISIBLE);
                else holder.bindingContact.tick.setVisibility(View.VISIBLE);
            });

        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }

        class ContactViewHolder extends RecyclerView.ViewHolder {
            private final ViewholderContactBinding bindingContact;

            public ContactViewHolder(ViewholderContactBinding binding) {
                super(binding.getRoot());
                this.bindingContact = binding;
            }
        }
    }

}