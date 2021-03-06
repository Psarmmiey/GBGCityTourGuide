package net.cloudcentrik.gbgcitytourguide;

/**
 * Created by Packard Bell on 2016-04-04.
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BaseMapFragment extends Fragment implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback{

    private String mapInfo;

    private MapView mMapView;
    private Bundle mBundle;

    private Double lad,lan;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    public static BaseMapFragment newInstance(String address, String contactInfo, String mapId) {
        BaseMapFragment fragment = new BaseMapFragment();
        Bundle args = new Bundle();
        args.putString("ADDRESS", address);
        args.putString("CONTACT_INFO", contactInfo);
        args.putString("MAP", mapId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_google_map, container, false);


        this.mapInfo = getArguments().getString("MAP", "");
        String mapDetails[]= TextUtils.split(this.mapInfo,",");

        lad=Double.parseDouble(mapDetails[0]); //lad
        lan=Double.parseDouble(mapDetails[1]);

        //map
        mMapView = (MapView) rootView.findViewById(R.id.mymap);
        mMapView.getMapAsync(this);
        mMapView.onCreate(mBundle);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        LatLng marker = new LatLng(lad,lan);
        map.addMarker(new MarkerOptions()
                .position(marker)
                .title(getActivity().getTitle().toString())
                .snippet(getArguments().getString("ADDRESS",""))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        );

        /*LatLng currentPosition = new LatLng(lad+0.002,lan-0.001);
        map.addMarker(new MarkerOptions()
                .position(currentPosition)
                .title("MY Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        );*/

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,15));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);

        /*Polyline line = map.addPolyline(new PolylineOptions()
                .add(new LatLng(lad+0.002,lan-0.001), new LatLng(lad, lan))
                .width(5)
                .color(Color.RED));*/

        enableMyLocation(map);
    }

    private void enableMyLocation(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    private int convertStringToId(String id) {
        Context context = getActivity().getApplicationContext();
        int resID = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
        return resID;
    }
}