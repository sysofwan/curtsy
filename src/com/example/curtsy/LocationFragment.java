package com.example.curtsy;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
/**
 * Created by SySofwan
 * Date: 8/3/13
 * Time: 7:03 PM
 */
public class LocationFragment extends Fragment implements
        LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public final static String FRAGMENT_TAG = "LocationFragment";
    private final static int MILISECONDS_PER_SECONDS = 1000;
    private final static int UPDATE_INTERVAL_IN_SECONDS = 60 * 2;
    private final static long UPDATE_INTERVAL = UPDATE_INTERVAL_IN_SECONDS * MILISECONDS_PER_SECONDS;
    private final static long FASTEST_INTERVAL = 10 * MILISECONDS_PER_SECONDS;
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private OnLocationAvailableListener mListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getActivity(), this, this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        Log.d(FRAGMENT_TAG, "Fragment created");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLocationAvailableListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnLocationAvailableListener");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if (servicesConnected()) {
            mLocationClient.connect();
        }
    }
    @Override
    public void onStop() {
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
        mLocationClient.disconnect();
        super.onStop();
    }
    @Override
    public void onConnected(Bundle dataBundle) {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
        mCurrentLocation = mLocationClient.getLastLocation();
        if (!isGpsAvailable()) {
            showSettingsAlert();
        }
        Log.d(FRAGMENT_TAG, "Current location: " + mCurrentLocation);
        mListener.onLocationAvailable();
    }
    @Override
    public void onDisconnected() {
    }
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation =  location;
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this.getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }
    public boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(FRAGMENT_TAG, "Google Play Services available");
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this.getActivity(),
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getFragmentManager(), FRAGMENT_TAG);
            }
            return false;
        }
    }
    private void showErrorDialog(int errorCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
                this.getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
        if (errorDialog != null) {
            ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
            dialogFragment.setDialog(errorDialog);
            dialogFragment.show(getFragmentManager(),FRAGMENT_TAG);
        }
    }
    public Location getCurrentLocation() {
        if (isGpsAvailable() && servicesConnected()) {
            return mCurrentLocation;
        }
        return null;
    }
    public boolean isGpsAvailable() {
        return mCurrentLocation != null && mLocationClient.isConnected();
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());
        alertDialog.setTitle(R.string.GPS_Alert_Dialog_Name);
        alertDialog.setMessage(R.string.Change_Settings_Dialog_Text);
        alertDialog.setPositiveButton(R.string.Settings_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                dialog.cancel();
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.Cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}