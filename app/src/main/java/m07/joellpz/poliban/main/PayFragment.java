package m07.joellpz.poliban.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentPayBinding;
import m07.joellpz.poliban.databinding.ViewholderTransactionCardBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentPayBinding binding;

    public PayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PayFragment.
     */

    public static PayFragment newInstance(String param1, String param2) {
        PayFragment fragment = new PayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        binding.bizumButtonPay.setOnClickListener(l -> definePaymentPane("bizum"));
        binding.transferButtonPay.setOnClickListener(l -> definePaymentPane("transfer"));
        binding.creditButtonPay.setOnClickListener(l -> definePaymentPane("credit"));
        //binding.investButtonPay.setOnClickListener(l ->);
    }

    private void definePaymentPane(String button) {
        switch (button) {
            case "bizum":
                binding.infotextView.setText("How many money do you need?");

                binding.firstEditText.setVisibility(View.GONE);
                binding.secondEditTextPay.setHint("Subject*");

                binding.sendButton.setText("SEND");
                break;

            case "credit":
                binding.infotextView.setText("How many money do you need?");

                binding.firstEditText.setVisibility(View.VISIBLE);
                binding.firstEditText.setHint("Months to be returned*");
                binding.firstEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                binding.secondEditTextPay.setHint("Reason*");

                binding.sendButton.setText("REQUEST");
                break;

            case "transfer":
                binding.infotextView.setText("Make a transfer in the easiest way!");

                binding.firstEditText.setVisibility(View.VISIBLE);
                binding.firstEditText.setHint("IBAN*");
                binding.firstEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                binding.secondEditTextPay.setHint("Reason*");

                binding.sendButton.setText("SEND");
                break;
        }
    }
}