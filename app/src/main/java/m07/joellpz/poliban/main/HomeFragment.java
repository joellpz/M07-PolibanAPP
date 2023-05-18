package m07.joellpz.poliban.main;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mrtyvz.archedimageprogress.ArchedImageProgressBar;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentHomeBinding;
import m07.joellpz.poliban.databinding.ViewholderBankAccountBinding;
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
    private ArchedImageProgressBar polibanArcProgress;
    private FirebaseUser user;
    private Toolbar toolbar;
    private BottomNavigationView bottomMenu;
    private ConstraintLayout mainView;
    protected static ViewPager viewPager;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;

    //ViewPagerAdapter adapter;
    AppViewModel appViewModel;


    public HomeFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();


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
        binding.recyclerViewHome.setAdapter(new BankAccountAdapter(options));

        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.findViewById(R.id.profileAppBarImage).setOnClickListener(l -> navController.navigate(R.id.profileFragment));
        bottomMenu = requireActivity().findViewById(R.id.bottomMainMenu);

        binding.mainView.setVisibility(View.GONE);

        navController = Navigation.findNavController(view);


        new ChargingImage(binding.customImageProgressBar, this);
        binding.customImageProgressBar.setVisibility(View.VISIBLE);

//        viewPager = view.findViewById(R.id.viewPager);
//        tabLayout = view.findViewById(R.id.tabLayout);

        view.findViewById(R.id.chatbotBtn).setOnClickListener(l -> navController.navigate(R.id.chatBotFragment));

        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        //adapter = new ViewPagerAdapter(getChildFragmentManager());
//        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);

        System.out.println("FIN ********************************************************");


        //recargarTabbedLayout(0);
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

    //TODO Testear
//    public void recargarTabbedLayout(int a) {
////        viewPager.setCurrentItem(0);
//        System.out.println("RECARGAAANDO ************************************");
//
//        appViewModel.getUserAccounts(user, new AppViewModel.petitionCallback() {
//            @Override
//            public void onCallback(List<BankAccount> value) {
//                appViewModel.setBankAccountList(value);
//                System.out.println("AHAHAHAHAHAHAHAAHSDHJKLASGDAHJS*************************");
//            }
//        });
//        System.out.println("AAAHAAA***************************");
//        List<Fragment> fragmentList = new ArrayList<>();
//        for (int i = 0; i < appViewModel.getBankAccountList().size() - a; i++) {
//            fragmentList.add(new IbanMainFragment());
//        }
//        fragmentList.add(new RegisterIbanFragment());
//        adapter.fragmentList = fragmentList;
//
//        adapter.notifyDataSetChanged();
//        tabLayout.removeAllTabs();
//        for (int i = 0; i < fragmentList.size()-1; i++) {
//            TabLayout.Tab tab = tabLayout.newTab();
//            tabLayout.addTab(tab);
//        }
//        viewPager.setAdapter(adapter);
//        viewPager.setCurrentItem(0);
//        tabLayout.setupWithViewPager(viewPager);
//    }
//    public void removeFragment() {
//        adapter.removeFragment(viewPager.getCurrentItem());
//    }
    public int getFragmentPosition() {
        return viewPager.getCurrentItem();
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

    private class BankAccountAdapter extends FirestoreRecyclerAdapter<BankAccount, RecyclerView.ViewHolder> {
        private static final int ITEM_TYPE_ADD_ACCOUNT = 1;

        public BankAccountAdapter(@NonNull FirestoreRecyclerOptions<BankAccount> options) {
            super(options);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_ADD_ACCOUNT)
                return new RegisterBankAccountViewHolder(ViewholderRegisterBankAccountBinding.inflate(getLayoutInflater(), parent, false));
            else
                return new BankAccountViewHolder(ViewholderBankAccountBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
        }

        @NonNull
        @Override
        public BankAccount getItem(int position) {
            if (position == getItemCount() - 1) return super.getItem(position-1);
            else return super.getItem(position);
        }

        @Override
        protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull BankAccount bankAccount) {
            System.out.println(getItemCount() + "+++++++++++++++++++++");
            if (getItemViewType(position) == ITEM_TYPE_ADD_ACCOUNT) {
                System.out.println(position + "Registrer");
                ((RegisterBankAccountViewHolder) holder).bind();
            } else {
                System.out.println(position + "BankAccounts");
                ((BankAccountViewHolder) holder).bind(bankAccount);
            }
        }

        @Override
        public int getItemCount() {
            return super.getItemCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) return ITEM_TYPE_ADD_ACCOUNT;
            else return super.getItemViewType(position);
        }
    }

    private class BankAccountViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderBankAccountBinding binding;

        public BankAccountViewHolder(ViewholderBankAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BankAccount bankAccount) {
            // Aquí puedes enlazar los datos de la cuenta bancaria con las vistas del item
            binding.ibanNumber.setText(bankAccount.getIban());
            binding.moneyBankInfo.setText(bankAccount.getBalanceString());
            binding.ownerInfo.setText(bankAccount.getOwner());
            if (bankAccount.getCif() != null) {
                binding.cifInfo.setText(bankAccount.getCif());
                binding.investButton.setOnClickListener(l -> {
                    AlertDialog ad = new AlertDialog.Builder(getContext()).create();
                    ad.setMessage("Have not been implemented yet...");
                    ad.setCancelable(true);
                    ad.show();
                });
            } else {
                binding.cifInfo.setVisibility(View.INVISIBLE);
                binding.investButton.setVisibility(View.GONE);
            }
            if (bankAccount.getIban().split(" ").length > 2) {
                if (bankAccount.getIban().split(" ")[1].equals("2100")) {
                    binding.bankData.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.lacaixa, requireActivity().getTheme())));
                    binding.bankEntityLogo.setImageResource(R.drawable.logo_lacaixa);
                } else if (bankAccount.getIban().split(" ")[1].equals("0057")) {
                    binding.bankData.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.bbva, requireActivity().getTheme())));
                    binding.bankEntityLogo.setImageResource(R.drawable.logo_bbva);
                } else if (bankAccount.getIban().split(" ")[1].equals("0049")) {
                    binding.bankData.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.santander, requireActivity().getTheme())));
                    binding.bankEntityLogo.setImageResource(R.drawable.logo_santander);
                }
            }
        }
    }

    private class RegisterBankAccountViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderRegisterBankAccountBinding binding;

        public RegisterBankAccountViewHolder(ViewholderRegisterBankAccountBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind() {
            binding.acceptButton.setOnClickListener(l -> {
                if (validateForm()) {
                    BankAccount account = new BankAccount(user.getUid(), binding.ibanEditText.getText().toString(), binding.ownerEditText.getText().toString());
                    account.saveBankAccountToUser((isSaved -> {
                        if (isSaved) {
                            // La cuenta se guardó correctamente
                            binding.ibanEditText.setError(null);
                            //TODO MIRAR ESTO para que haga redireccion al nuevo
                            //TODO Tambien mirar como marcar que una List<Transacionts> sea una nueva coleccion dentro de un documento.
                            HomeFragment.this.binding.recyclerViewHome.scrollToPosition(2);
                        } else {
                            // La cuenta ya existe o hubo un error al guardar
                            binding.ibanEditText.setError("This IBAN is already registered!");
                        }
                    }));
                }
            });
        }

        public boolean validateForm() {
            boolean valid = true;
            if (TextUtils.isEmpty(binding.ibanEditText.getText().toString())) {
                binding.ibanEditText.setError("Required.");
                valid = false;
            } else {
                binding.ibanEditText.setError(null);
            }

            if (TextUtils.isEmpty(binding.ownerEditText.getText().toString())) {
                binding.ownerEditText.setError("Required.");
                valid = false;
            } else {
                binding.ownerEditText.setError(null);
            }

            return valid;
        }
    }
}

