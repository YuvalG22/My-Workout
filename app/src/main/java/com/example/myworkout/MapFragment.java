package com.example.myworkout;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myworkout.Intefaces.OnWorkoutDataUpdateListener;
import com.example.myworkout.Utilities.CustomLatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private OnWorkoutDataUpdateListener dataUpdateListener;
    private LocationRequest locationRequest;
    private LatLng lastLocation;
    private double totalDistance = 0;
    private double pace;
    private long elapsedTime = 0;
    private Polyline polyline;
    private List<CustomLatLng> pathPoints = new ArrayList<>();

    public MapFragment() {
        // Required empty public constructor
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnWorkoutDataUpdateListener) {
            dataUpdateListener = (OnWorkoutDataUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnWorkoutDataUpdateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();
        onMapReady(mMap);
        getCurrentLocation();
        return v;
    }

    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(current).title("Start"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 17));
                }
            }
        });
    }

    public void startTracking() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            for (Location location : locationResult.getLocations()) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CustomLatLng customLatLng = CustomLatLng.fromLatLng(latLng);
                pathPoints.add(customLatLng);
                updateMapPath();

                if (lastLocation != null) {
                    double distance = calculateDistance(lastLocation, latLng);
                    totalDistance += distance;
                    pace = (elapsedTime / 1000.0) / (totalDistance / 1000.0);

                    // Notify the activity
                    dataUpdateListener.onDistanceUpdated(totalDistance);
                    dataUpdateListener.onPaceUpdated(pace);
                }
                lastLocation = latLng;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        }
    };

    private double calculateDistance(LatLng lastLocation, LatLng currentLocation) {
        float[] results = new float[1];
        Location.distanceBetween(
                lastLocation.latitude, lastLocation.longitude,
                currentLocation.latitude, currentLocation.longitude,
                results);
        return results[0];
    }

    public void updateElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    private void updateMapPath() {
        if (polyline == null) {
            List<LatLng> mapPoints = convertToLatLng(pathPoints);
            polyline = mMap.addPolyline(new PolylineOptions().addAll(mapPoints).color(Color.BLUE).width(12));
        } else {
            polyline.setPoints(convertToLatLng(pathPoints));
        }
    }

    private List<LatLng> convertToLatLng(List<CustomLatLng> customLatLngs) {
        List<LatLng> latlngs = new ArrayList<>();
        for (CustomLatLng customLatLng : customLatLngs) {
            latlngs.add(customLatLng.toLatLng());
        }
        return latlngs;
    }

    public void stopTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public double getTotalDistance(){
        return totalDistance;
    }

    public double getPace(){
        return pace;
    }

    public List<CustomLatLng> getPathPoints() {
        return pathPoints;
    }
}