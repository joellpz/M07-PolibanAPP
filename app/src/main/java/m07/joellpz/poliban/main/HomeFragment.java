package m07.joellpz.poliban.main;

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
import androidx.fragment.app.FragmentStatePagerAdapter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;
import m07.joellpz.poliban.model.WalletCard;
import m07.joellpz.poliban.tools.ChargingImage;
import m07.joellpz.poliban.view.ChatBotFragment;
import m07.joellpz.poliban.view.IbanMainFragment;
import m07.joellpz.poliban.view.RegisterIbanFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment #newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public NavController navController;

    private ArchedImageProgressBar polibanArcProgress;

    private FirebaseUser user;
    private Toolbar toolbar;
    private BottomNavigationView bottomMenu;
    private ConstraintLayout mainView;
    private Uri photoURL;

    protected static ViewPager viewPager;
    private TabLayout tabLayout;
    ViewPagerAdapter adapter;
    ChatBotFragment chatBotFragment;


    public HomeFragment() {
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.profileAppBarImage).setOnClickListener(l -> navController.navigate(R.id.profileFragment));
        bottomMenu = requireActivity().findViewById(R.id.bottomMainMenu);
        mainView = view.findViewById(R.id.mainView);
        mainView.setVisibility(View.GONE);

        navController = Navigation.findNavController(view);

        polibanArcProgress = view.findViewById(R.id.custom_imageProgressBar);
        new ChargingImage(polibanArcProgress, this);
        polibanArcProgress.setVisibility(View.VISIBLE);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

//        chatBotFragment = new ChatBotFragment();
//        getChildFragmentManager().beginTransaction().add(R.id.chatbotFrame, chatBotFragment).commit();
        view.findViewById(R.id.chatbotBtn).setOnClickListener(l -> navController.navigate(R.id.chatBotFragment));

        List<BankAccount> bankAccounts = new ArrayList<>();
        List<WalletCard> walletCards = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        List<Transaction> futureTransactions = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            Date randomDate = new Date(ThreadLocalRandom.current()
                    .nextLong(1669852148000L, 1677538800000L));
            Transaction transaction = new Transaction("Titus",false, (float) (Math.random() * 158) - 79, "La Fiesta", randomDate);
            transactions.add(transaction);
        }


        for (int i = 0; i < Math.random() * 4; i++) {
            WalletCard walletCard = new WalletCard((float) (Math.random() * 158), "4241 3373 0328 3409", "Joel Lopez", 739, new Date(), true);
            walletCards.add(walletCard);
        }

        for (int i = 1; i < 3; i++) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(new Date());
            calendar.set(Calendar.DAY_OF_MONTH, 26 + i);
            futureTransactions.add(new Transaction("El PUIG",true, (float) (Math.random() * 158) - 79, "Nomina"+i, calendar.getTime()));

        }

//        for (int i = 0; i < 4; i++) {
//            BankAccount bankAccount = new BankAccount("ES54 2095 5178 7932 1818 3952", "Joel Lopez", null, (float) (Math.random() * 4380), transactions, walletCards);
//            bankAccounts.add(bankAccount);
//        }

        bankAccounts.add(new BankAccount("ES54 0049 5178 7932 1818 3952", "Joel Lopez", null, (float) (Math.random() * 4380), transactions, futureTransactions, walletCards));
        bankAccounts.add(new BankAccount("ES54 0057 5178 7932 1818 3952", "Joel Lopez", null, (float) (Math.random() * 4380), transactions, futureTransactions, walletCards));
        bankAccounts.add(new BankAccount("ES54 2100 5178 7932 1818 3952", "Joel Lopez", null, (float) (Math.random() * 4380), transactions, futureTransactions, walletCards));

        adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (BankAccount bank : bankAccounts) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("bank", bank);
            IbanMainFragment ibanMainFragment = new IbanMainFragment();
            ibanMainFragment.setArguments(bundle);

            adapter.addFragment(ibanMainFragment);
        }
        adapter.addFragment(new RegisterIbanFragment());

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

    public void removeFragment() {
        adapter.removeFragment(viewPager.getCurrentItem());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        //private List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getItemPosition(Object object) {
            return this.POSITION_NONE;
        }

        @NonNull
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

        public void removeFragment(int position) {

            fragmentList.remove(position);
            notifyDataSetChanged();
            viewPager.setCurrentItem(position - 1, false);
        }


    }
}