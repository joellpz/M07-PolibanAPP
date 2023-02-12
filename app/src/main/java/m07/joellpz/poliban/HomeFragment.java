package m07.joellpz.poliban;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import java.util.ArrayList;
import java.util.List;

import m07.joellpz.poliban.tools.ChargingImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private NavController navController;

    private ArchedImageProgressBar polibanArcProgress;

    private FirebaseUser user;
    private Toolbar toolbar;
    private BottomNavigationView bottomMenu;
    private ConstraintLayout mainView;
    private Uri photoURL;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public HomeFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = (Toolbar) requireActivity().findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.profileAppBarImage).setOnClickListener(l -> navController.navigate(R.id.profileFragment));
        bottomMenu = (BottomNavigationView) requireActivity().findViewById(R.id.bottomMainMenu);
        mainView = view.findViewById(R.id.mainView);
        mainView.setVisibility(View.GONE);

        navController = Navigation.findNavController(view);
        //view.findViewById(R.id.chatbotBtn).setOnClickListener(l -> );
        polibanArcProgress = view.findViewById(R.id.custom_imageProgressBar);
        new ChargingImage(polibanArcProgress, this);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new IbanMainFragment());
        adapter.addFragment(new IbanMainFragment());
        adapter.addFragment(new IbanMainFragment());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        user = FirebaseAuth.getInstance().getCurrentUser();
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
                    mainView.setVisibility(View.VISIBLE);
                    polibanArcProgress.setVisibility(View.GONE);
                });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO IMPORATNTE ESTO DE CAMBIAR LA TOOLBAR


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        //private List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titleList.get(position);
//        }

        public void addFragment(Fragment fragment/*,String title*/) {
            fragmentList.add(fragment);
            //titleList.add(title);
        }
    }
}