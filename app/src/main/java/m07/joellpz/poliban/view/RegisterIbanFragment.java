package m07.joellpz.poliban.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import m07.joellpz.poliban.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterIbanFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterIbanFragment extends Fragment {
    
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
        return inflater.inflate(R.layout.fragment_register_iban, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.acceptButton).setOnClickListener(l -> requireActivity().recreate());
    }
}