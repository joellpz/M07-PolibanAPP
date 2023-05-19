package m07.joellpz.poliban.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.BankAccountAdapter;
import m07.joellpz.poliban.databinding.FragmentHomeBinding;
import m07.joellpz.poliban.databinding.ViewholderRegisterBankAccountBinding;
import m07.joellpz.poliban.model.AppViewModel;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.tools.ChargingImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    public NavController navController;
    private FirebaseUser user;
    private Toolbar toolbar;
    private BottomNavigationView bottomMenu;

    public HomeFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(view);


        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewHome.setHasFixedSize(true);
        LinearSnapHelper snapHelper = new LinearSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager lm, int velocityX, int velocityY) {
                View centerView = findSnapView(lm);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = lm.getPosition(centerView);
                int targetPosition = -1;
                if (lm.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (lm.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = lm.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(binding.recyclerViewHome);
        FirebaseFirestore.getInstance().collection("bankAccount").whereEqualTo("userId", user.getUid()).get().addOnSuccessListener(docSnap -> docSnap.forEach(System.out::println));
        Query q = FirebaseFirestore.getInstance().collection("bankAccount").whereEqualTo("userId", user.getUid());
        FirestoreRecyclerOptions<BankAccount> options = new FirestoreRecyclerOptions.Builder<BankAccount>()
                .setQuery(q, BankAccount.class)
                .setLifecycleOwner(this)
                .build();
        binding.recyclerViewHome.setAdapter(new BankAccountAdapter(options, this, user));

        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.profileAppBarImage).setOnClickListener(l -> navController.navigate(R.id.profileFragment));
        bottomMenu = requireActivity().findViewById(R.id.bottomMainMenu);

        binding.mainView.setVisibility(View.GONE);


        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        view.findViewById(R.id.chatbotBtn).setOnClickListener(l -> navController.navigate(R.id.chatBotFragment));

        FirebaseFirestore.getInstance().collection("users").document(user.getUid())
                .get().addOnSuccessListener(docSnap -> {
                    if (docSnap.exists()) {
                        if (docSnap.get("profilePhoto") != null)
                            Glide.with(requireContext()).load(docSnap.get("profilePhoto")).circleCrop().into((ImageView) toolbar.findViewById(R.id.profileAppBarImage));
                        else
                            Glide.with(requireContext()).load(R.drawable.profile_img).circleCrop().into((ImageView) toolbar.findViewById(R.id.profileAppBarImage));
                    }
                    bottomMenu.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    binding.mainView.setVisibility(View.VISIBLE);
                    binding.customImageProgressBar.setVisibility(View.GONE);
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentHomeBinding.inflate(inflater, container, false)).getRoot();
    }
}

