package com.bignerdranch.andriod.locatr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//using SupportMapFragment class to automatically display
//get all the callback methods for the Maps Api
public class LocatrFragment extends SupportMapFragment {

    public static final String TAG = "LocatrFragment";

    /*Also to to handle permissions at runtime requires 3 things:
     * check to see whether you have permission before performing the operation
     * request permission if you do not already have it
     * listen for the response to your permissions request*/
    /*Also depending on the situation maybe the standard
     * notification is not enough to properly explain to the user
     * so a rationale might be in order*/
    /*get permission for findImage() via an array of permissions ready to be used
     * dangerous permission as always asked in groups since they usually deal
     * with the same kind of access*/
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    //fields to store the current state
    //of the map
    private  Bitmap mMapImage;
    private GalleryItem mMapItem;
    private Location mCurrentLocation;

    //get reference to GoogleMap master Object
    private GoogleMap mMap;



    /*To Consume the Google Play Services API
    to create an instance of GoogleApiClient class
     */

    private GoogleApiClient mClient;


    public static LocatrFragment newInstance() {
        return new LocatrFragment();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        /* To create the client create a GoogleApiClient.Builder
         * and create it via the addApi method the connection state is
         * managed by two interfaces: ConnectionCalbacks and
         * OnConnectionFailedListener */
        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        //call getMapAsync to get the proper asynchronous callback reference
        //to googleMap
        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                updateUI();
            }
        });


    }



    @Override
    public void onStart() {
        super.onStart();

        // once client is connect menu options
        //will change make sure to disable it
        getActivity().invalidateOptionsMenu();

        //to use the client must connect in onStart()
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        //must disconnect in onStop()
        mClient.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);

        //check to see if Service is Connected and
        //act accordingly
        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(mClient.isConnected());
    }

    //hook up the findImage() method to your search button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                //check to see if we have permission first
                if (hasLocationPermission()) {
                    findImage();
                } else {
                    //asynchronous request to display system dialog with
                    //a message appropiate to the permission you are questing
                    requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*The requestPermissions method's callback method to tell
     * the program whether the user ALLOW or DENY*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //use switch method for multiple cases if needed
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                //our hasLocationPermission does the same job as grantResults does
                if (hasLocationPermission()) {
                    findImage();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    /*use this method to get a fix location
     * via the FusedLocationProviderApi's singleton
     * object called FusedLocationApi that lives in
     * each LocationServices. Each location is
     * respresented a different LocationService */

    private void findImage() {
        LocationRequest request = LocationRequest.create();

        //priority how accurate
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //how times to update
        request.setNumUpdates(1);

        //how times to repeat
        request.setInterval(0);

        /* use to send off this request and listen for the Locations
        that come back. don't need to call removeLocationUpdates
        since setNumUpdates is 1 */
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.i(TAG, "Got a fix: " + location);
                    //make new instance of SearchTask to
                    //fetch the photo
                    new SearchTask().execute(location);
                }
            });
        } catch (SecurityException se) {
            Log.i(TAG, "Unable to get location: " + se);
            se.printStackTrace();
        }

    }

    //method used to check if we have access to the first permission
    //within the group. If one is granted, then all are granted.
    private boolean hasLocationPermission() {
        //using ContextCompat to deal with backward compatability
        int result = ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        //if result is equal or not... true or false
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /*add update/populate UI method*/
    private void updateUI(){
        //nothing came back
        if(mMap == null || mMapImage == null){
            return;
        }

        /*get coordinates using LatLng class*/
        LatLng itemPoint = new LatLng(mMapItem.getLat(), mMapItem.getLon());
        LatLng myPoint = new LatLng(
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()
        );

        /*creating objects that describe what the GoogleMap
        * is suppose to create*/
        BitmapDescriptor itemBitmap = BitmapDescriptorFactory.fromBitmap(mMapImage);
        MarkerOptions itemMarker = new MarkerOptions()
                .position(itemPoint)
                .icon(itemBitmap);
        MarkerOptions myMarker = new MarkerOptions()
                .position(myPoint);

        mMap.clear();

        /*adding the marker descriptors created
        * from the MarkerOptions class*/
        mMap.addMarker(itemMarker);
        mMap.addMarker(myMarker);

        /*paramter used in CameraUpdate to move the map around
        * like a rectangle give SW and NE corner coordinates
         * can always just use four points to make things easier*/
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(itemPoint)
                .include(myPoint)
                .build();
        /*paramter used in CameraUpdate to zoom the map in*/
        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        /*CameraUpdate is used to move the Map around via
        * different static methods to adjust the zoom, position, etc
        * here we adjusted the position and the zoom */
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        //could also use moveCamera but animate more fun :)
        mMap.animateCamera(update);

    }

    //using AsyncTask to make a SearchTask to fetch the
    //photos from the fetched location
    private class SearchTask extends AsyncTask<Location, Void, Void> {
        private GalleryItem mGalleryItem;
        private Bitmap mBitmap;
        private Location mLocation;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Location... locations) {
            mLocation = locations[0];
            FlickrFetchr fetchr = new FlickrFetchr();
            List<GalleryItem> items = fetchr.searchPhotos(locations[0]);

            if (items.size() == 0) {
                return null;
            }

            mGalleryItem = items.get(0);

            //use getUrlBytes helper method to get bytes so that
            //it can be decoded into an image
            try {
                byte[] bytes = fetchr.getUrlBytes(mGalleryItem.getUrl());
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (IOException ioe) {
                Log.i(TAG, "Unable to download bitmap", ioe);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            //assign values from location to map
            //to allow MapView within SupportMapFragment
            //populate the data on the screen
            mMapImage = mBitmap;
            mMapItem = mGalleryItem;
            mCurrentLocation = mLocation;

            updateUI();


        }
    }




    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }
}
