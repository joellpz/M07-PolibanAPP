package m07.joellpz.poliban.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.IdRes;
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

import java.util.Objects;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.BankAccountAdapter;
import m07.joellpz.poliban.databinding.FragmentHomeBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.tools.ChargingImage;

/**
 * A fragment that represents the home screen of the application.
 */
public class HomeFragment extends Fragment {
    /**
     * View binding for the fragment.
     */
    private FragmentHomeBinding binding;
    /**
     * Navigation controller for navigating between fragments.
     */
    public NavController navController;
    /**
     * Toolbar Object from the Activity
     */
    private Toolbar toolbar;
    /**
     * BottomMenu Object from the Activity
     */
    private BottomNavigationView bottomMenu;

    /**
     * Default constructor for HomeFragment.
     */
    public HomeFragment() {
    }

    /**
     * Called when the fragment's view is created.
     *
     * @param view               The fragment's root view
     * @param savedInstanceState The saved instance state bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("**************** HOME FRAGMENT ****************");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(view);

        // Set up RecyclerView
        binding.recyclerViewHome.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
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
        binding.recyclerViewHome.setNestedScrollingEnabled(false);

        // Set up Firestore query and options for BankAccountAdapter
        Query q = FirebaseFirestore.getInstance().collection("bankAccount").whereEqualTo("userId", Objects.requireNonNull(user).getUid());
        FirestoreRecyclerOptions<BankAccount> options = new FirestoreRecyclerOptions.Builder<BankAccount>()
                .setQuery(q, BankAccount.class)
                .setLifecycleOwner(this)
                .build();
        binding.recyclerViewHome.setAdapter(new BankAccountAdapter(options, this, user, false));

        // Set up toolbar and bottom menu
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.profileAppBarImage).setOnClickListener(l -> navController.navigate(R.id.profileFragment));
        bottomMenu = requireActivity().findViewById(R.id.bottomMainMenu);

        // Hide main view and display progress bar
        binding.mainView.setVisibility(View.GONE);
        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

        // Set up click listener for chatbot button
        view.findViewById(R.id.chatbotBtn).setOnClickListener(l -> navController.navigate(R.id.chatBotFragment));

        // Load user profile photo and show main view and toolbar
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
                });
    }

    /**
     * Called when the fragment is creating its view hierarchy.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here
     * @return The root view of the fragment, or null
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentHomeBinding.inflate(inflater, container, false)).getRoot();
    }
}
