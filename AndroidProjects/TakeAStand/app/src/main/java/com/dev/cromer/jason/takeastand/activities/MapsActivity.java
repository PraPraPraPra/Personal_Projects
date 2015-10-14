package com.dev.cromer.jason.takeastand.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.dev.cromer.jason.takeastand.Logic.PostUserMarkerHandler;
import com.dev.cromer.jason.takeastand.Logic.RetrieveAllMarkersHandler;
import com.dev.cromer.jason.takeastand.R;
import com.dev.cromer.jason.takeastand.networking.GenericHttpGetRequest;
import com.dev.cromer.jason.takeastand.networking.HttpMarkerPostRequest;
import com.dev.cromer.jason.takeastand.objects.MarkerPostRequestParams;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
                                                                GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    protected Intent mapsIntent;
    private int userReligionID;
    private boolean initialLocationShown = false;

    //Constants
    private static final String POST_MARKER_URL = "http://takeastandapi.elasticbeanstalk.com/add_marker";
    private static final String GET_MARKERS_URL = "http://takeastandapi.elasticbeanstalk.com/get_markers";
    private static final String USER_RELIGION_CHOICE_EXTRA = "USER_CHOICE_EXTRA";
    private static final int LOCATION_REQUEST_INTERVAL_MILLISECONDS = 10000;
    private static final int FASTEST_LOCATION_REQUEST_INTERVAL_MILLISECONDS = 5000;
    private static final float CAMERA_ZOOM = 3;
    private static final int RETURN_FAILED = -1;
    private static final int RETURN_SUCCESS = 0;

    //Religion integer constants
    private static final int INT_CHRISTIAN = 1;
    private static final int INT_ISLAM = 2;
    private static final int INT_CATHOLIC = 3;
    private static final int INT_HINDU = 4;
    private static final int INT_BUDDHIST = 5;
    private static final int INT_AGNOSTIC = 6;
    private static final int INT_ATHIEST = 7;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setUpGoogleApiClient();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getExtras();

        retrieveAllMarkers();
    }



    private void setUpGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false); //disable UI features
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMyLocationButtonClickListener(this);
    }



    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REQUEST_INTERVAL_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_LOCATION_REQUEST_INTERVAL_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    //This method shows our current location only once each time the user opens the app
    private void showLocation(Location myLocation){
        final int result;

        if(myLocation != null){
            if(!initialLocationShown){
                initialLocationShown = true;

                //post user's marker to database
                result = postUserMarker(myLocation);

                //Handle result in case marker could not be posted
                processPostRequestResult(result);
            }

            //Small focus zoom on user's location
            LatLng latlngLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngLocation, CAMERA_ZOOM));
        }
    }


    private void getExtras(){
            /*
        This method retrieves the integer associated with the user's religion.
        The integers correspond to the position on the spinner that the user chooses from, i.e.:
        1 = Christian
        2 = Islam
        3 = Catholic
        ..and so on.
        The default is set to Agnostic in the event that the extra did not go through,
        as Agnostic is a more neutral viewpoint.
     */
        mapsIntent = getIntent();
        userReligionID = mapsIntent.getIntExtra(USER_RELIGION_CHOICE_EXTRA, INT_AGNOSTIC);
    }

    private int postUserMarker(Location myLocation){
        final int result;

        if(myLocation != null){

            //Populate the params object
            MarkerPostRequestParams markerParams = new MarkerPostRequestParams(POST_MARKER_URL,
                    (float)myLocation.getLatitude(), (float)myLocation.getLongitude(), userReligionID);

            //Create a new instance of the http post request class
            HttpMarkerPostRequest markerPostRequest = new HttpMarkerPostRequest();

            //Perform the post request using our parameter and request object
            PostUserMarkerHandler userMarkerHandler = new PostUserMarkerHandler(markerPostRequest, markerParams);
            result = userMarkerHandler.makeHttpMarkerPostRequest();

            return result;
        }
        return RETURN_FAILED;
    }

    private void processPostRequestResult(int result){
        //Perform any post processing if needed
        if(result == RETURN_SUCCESS){
            //Success
            Log.d("TAG", "SUCCESS");
        }
        else if(result == RETURN_FAILED){
            //failed
            Log.d("TAG", "SUCCESS");
        }
    }

    private void retrieveAllMarkers(){
        final String result;

        //Create new instance of a GET request
        GenericHttpGetRequest getRequest = new GenericHttpGetRequest();

        //Pass our request object and URL into the handler class
        RetrieveAllMarkersHandler markersHandler = new RetrieveAllMarkersHandler(GET_MARKERS_URL, getRequest);

        //Retrieve the string result
        result = markersHandler.fetchMarkers();
        markersHandler.parseMarkerResult(result);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Resume location updates
        if(mGoogleApiClient.isConnected()){
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop location updates to conserve batttery
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Oh no! Looks like we've got some network issues.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        final Location myLocation = mMap.getMyLocation();
        if(myLocation != null){
            showLocation(myLocation);
        }
        return true;
    }

}
