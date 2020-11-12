package com.example.paint.ui.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.paint.MapCanvas;
import com.example.paint.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    private Polyline path;
    private ArrayList<LatLng> paths = new ArrayList<>();

    MapCanvas mapCanvas;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location userLocation;
    private GoogleMap mMap;
    private MapView mapView;

    private Button drawButton;

    private boolean drawState;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        drawButton = view.findViewById(R.id.drawingButton);
        drawState = false;

        // Check if the permissions are granted
        // if not, request permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        else {
            locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

            locationListener = location -> {
                if (userLocation == null)
                    userLocation = new Location(location);
                else userLocation.set(location);

                LatLng positionLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(positionLatLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, 15), 2000, null);

                if (drawState) {
                    paths.add(positionLatLng);
                    path = mMap.addPolyline(new PolylineOptions()
                            .clickable(true).addAll(paths));
                }
            };

            //get instant location
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);

            drawButton.setOnClickListener(v -> {
                if (!drawState) {
                    drawState = true;
                    drawButton.setText("STOP DRAWING");
                } else {
                    drawState = false;
                    drawButton.setText("START DRAWING");
                }
            });

//            mapCanvas = new MapCanvas(getContext(), null, userLocation);
//            // define Canvas as layout view
//            ConstraintLayout layout = view.findViewById(R.id.mapsLayout);
//            layout.addView(mapCanvas);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Continue the action or workflow
                // in your app.
                Toast.makeText(getContext(), "permission was granted", Toast.LENGTH_SHORT).show();
            } else {
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
                Toast.makeText(getContext(), "PERMISSION REQUIRED", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        LatLng positionLatLng;

        if (userLocation != null) {
            positionLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(positionLatLng)
                    .title("You are here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(positionLatLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, 15), 2000, null);
        }

        // Set listeners for click events.
        // NOT USED
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}