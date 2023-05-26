package m07.joellpz.poliban.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Objects;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.adapter.TransactionAdapter;
import m07.joellpz.poliban.databinding.FragmentMapsBinding;
import m07.joellpz.poliban.model.BankAccount;
import m07.joellpz.poliban.model.Transaction;

/**
 * The MapsFragment class represents a fragment that displays a map with markers representing transactions.
 * It allows users to view transactions and interact with them.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    /**
     * The bank account to be displayed on the map.
     */
    private BankAccount bankAccount;

    /**
     * The ClusterManager instance for managing clustered markers.
     */
    private ClusterManager<Transaction> clusterManager;

    /**
     * The binding object for accessing views in the fragment.
     */
    private FragmentMapsBinding binding;

    /**
     * The NavController instance for navigating between destinations.
     */
    private NavController navController;

    /**
     * Constructs a new instance of MapsFragment with a specified bank account.
     *
     * @param bankAccount the bank account to display on the map
     */
    public MapsFragment(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * Constructs a new instance of MapsFragment.
     */
    public MapsFragment() {

    }

    /**
     * Called when the map is ready to be used.
     *
     * @param googleMap the GoogleMap instance representing the map
     */
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(41.455775193431435, 2.201906692392249)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        clusterManager = new ClusterManager<>(requireContext(), googleMap);

        addMarkers();

        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        clusterManager.setOnClusterItemClickListener(clusterManager -> {
            Query qTransactionsDay = FirebaseFirestore.getInstance()
                    .collection("bankAccount")
                    .document(clusterManager.getBankId())
                    .collection("transaction")
                    .whereEqualTo("transactionId", clusterManager.getTransactionId());
            FirestoreRecyclerOptions<Transaction> options = new FirestoreRecyclerOptions.Builder<Transaction>().setQuery(qTransactionsDay, Transaction.class).setLifecycleOwner(this).build();
            binding.fragmentTransactionCardsMaps.recyclerviewTransactionCards.setAdapter(new TransactionAdapter(options, this, true));
            binding.fragmentTransactionCardsMaps.getRoot().setVisibility(View.VISIBLE);
            return true;
        });

        binding.fragmentTransactionCardsMaps.goBackBtnCards.setOnClickListener(l -> binding.fragmentTransactionCardsMaps.getRoot().setVisibility(View.INVISIBLE));
    }

    /**
     * Called when a marker is clicked on the map.
     *
     * @param marker the clicked marker
     * @return true if the click event is consumed, false otherwise
     */
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        System.out.println(marker.getTag());
        return false;
    }

    /**
     * Adds markers to the map representing transactions.
     */
    private void addMarkers() {
        Transaction.getQueryTransactions("month", null, bankAccount).get().addOnSuccessListener(docSnap -> {
            for (Transaction t : docSnap.toObjects(Transaction.class)) {
                if (t.getValue() < 0 && t.getUbi() != null) {
                    System.out.println(t);
                    clusterManager.addItem(t);
                }
            }
        });
    }

    /**
     * Creates the view for the fragment.
     *
     * @param inflater           the layout inflater
     * @param container          the container for the fragment
     * @param savedInstanceState the saved instance state
     * @return the fragment view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //TODO Mirar de crear un Fragmetn Chiquito para el BankAccountViewHolder que sea un extends del Grande pero sin NavController.
        // Alomejor así se soluciona lo de NavController al Girar la Pantalla.

        //TODO Usar MutableLiveData como en los ejemplos para asi guardar el BankAccount que se necesite en AppViewModel.
        if (bankAccount == null)
            this.bankAccount = (BankAccount) requireArguments().getSerializable("bankAccount");
        return (binding = FragmentMapsBinding.inflate(inflater, container, false)).getRoot();
    }

    /**
     * Called after the view has been created.
     *
     * @param view               the created view
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        if (navController == null) {
            navController = Navigation.findNavController(view);
        }

        binding.goBackBtnMapa.setOnClickListener(l -> navController.navigate(R.id.homeFragment));
    }
}
