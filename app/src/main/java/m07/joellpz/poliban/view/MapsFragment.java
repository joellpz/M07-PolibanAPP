package m07.joellpz.poliban.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import m07.joellpz.poliban.R;
import m07.joellpz.poliban.databinding.FragmentMapsBinding;

public class MapsFragment extends Fragment{

    //TODO Para poner los puntitos en el Maps
    // https://github.com/googlemaps/android-maps-utils
    private GoogleMap mMap;
    private FragmentMapsBinding binding;
    private NavController navController;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);


            LatLng badalona = new LatLng(41.455775193431435, 2.201906692392249);
            googleMap.addMarker(new MarkerOptions().position(badalona).title("El Puig"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(badalona));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }


    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return (binding = FragmentMapsBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        System.out.println("MAPS **************************************************");
        navController = Navigation.findNavController(view);
        //navController = requireParentFragment().requireView().findgetParentFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main).findNavController();



        binding.goBackBtnMapa.setOnClickListener(l -> navController.navigate(R.id.homeFragment));
    }


}